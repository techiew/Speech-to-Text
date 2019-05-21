package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1p1beta1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1p1beta1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.WordInfo;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;

// Klasse som bruker Google sin API for å finne ord med timestamps fra lydfiler
public class AnalyseFile {
	
	private ArrayList<Participant> participantList = new ArrayList<Participant>();
	private ArrayList<Float> usedWords = new ArrayList<Float>();
	private int participantChecker = 0;
	
	// Starter en analyse på filen som ligger klar på Google sin server
	// gcsUri er linken til filen på serveren
	public boolean analyseSoundFile(String gcsUri) {
		participantList.add(new Participant());
		
		// Starter en klient med GOOGLE_APPLICATION_CREDENTIALS
		try (SpeechClient speech = SpeechClient.create()) {
			
			// Konfigurasjonen på filen som vi lastet opp
			RecognitionConfig config = RecognitionConfig.newBuilder()
					.setEncoding(AudioEncoding.LINEAR16)
					.setLanguageCode("NO")
					.setEnableWordTimeOffsets(true)
					.build();
			
			RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
			
			// Her starter vi transkripsjonen, vi må vente på svar
			OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response = speech
					.longRunningRecognizeAsync(config, audio);
			
			while (!response.isDone()) {
				System.out.println("Venter på respons fra Google...");
				Thread.sleep(10000);
			}

			List<SpeechRecognitionResult> results = response.get().getResultsList();
			
			for (SpeechRecognitionResult result : results) {
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				System.out.printf("Transcription: %s\n", alternative.getTranscript());
				
				// Sjekker listen med ord
				for (WordInfo wordInfo : alternative.getWordsList()) {
					String word = wordInfo.getWord();

					// Finner start tiden
					float startNanosecond = (wordInfo.getStartTime().getNanos() / 100000000);
					float startSecond = wordInfo.getStartTime().getSeconds();
					float timeStampStart = startSecond + (startNanosecond / 10);
					
					// Finner slutt tiden
					float endNanosecond = (wordInfo.getEndTime().getNanos() / 100000000);
					float endSecond = wordInfo.getEndTime().getSeconds();
					float timeStampEnd = endSecond + (endNanosecond / 10);
					
					// Legg til ordet til riktig deltaker
					participantList.get(participantChecker).addWords(new Word(word, timeStampStart, timeStampEnd, participantChecker));
				}
				
			}
			
			participantChecker++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// Lager setningene ved å sjekke tid på ord, sjekker når noen andre begynner å snakke
	// Når noen andre har begynt å snakke, så er forrige setning komplett
	// Og da starter vi på neste
	public void constructSentences() {		
		String sentence = "";
		int totalWords = 0;
		int wordCount = 0;
		int prevOwner = -1;
		float firstWordTime = -1;
		float lastWordTime = 0;
		
		// Teller antall ord for alle medlemmer, sånn at vi kan holde styr på
		// hvor mange ord vi har gått gjennom senere når vi lager setninger
		for(int i = 0; i < participantList.size(); i++) {
			totalWords += participantList.get(i).getWords().size();
		}
		
		for (int z = 0; z < participantList.size(); z++) {

			for (int x = 0; x < participantList.get(z).getWords().size(); x++) {
				Word word = getNextWord(participantList);
				
				// Hvis samme person fortsatt snakker, legg til nåværende ord i setningen dems,
				// Hvis ikke, sjekk om setningen består av noe, hvis den gjør det, legg den ferdige setningen
				// til personen sin liste over setninger. Deretter starter vi med en gang å legge
				// det nåværende ordet inn i setningen til den nye personen som prater
				if (prevOwner == word.getOwner()) {
					sentence = sentence + " " + word.getWord();
					lastWordTime = word.getEndTime();
					if(firstWordTime == -1) firstWordTime = word.getStartTime();
					usedWords.add(word.getMeanTime());
				} else {
					
					if (sentence.length() > 0 && sentence.trim().length() > 0) {
						if(sentence.charAt(0) == ' ') sentence = sentence.substring(1);
						participantList.get(prevOwner).addSentence(new Sentence(sentence, firstWordTime, lastWordTime, prevOwner));
						sentence = "";
						firstWordTime = -1;
					}
						
					sentence = sentence + " " + word.getWord();
					lastWordTime = word.getEndTime();
					if(firstWordTime == -1) firstWordTime = word.getStartTime();
					usedWords.add(word.getMeanTime());
				}
				
				wordCount++;
				
				// Hvis det for en eller annen grunn er samme eier hele veien, 
				// så vil denne if-sjekken lagre setningen når alle ordene er gått over
				if(wordCount == totalWords) {
					
					if(sentence.length() > 0 && sentence.trim().length() > 0) {
						if(sentence.charAt(0) == ' ') sentence = sentence.substring(1);
						lastWordTime = word.getEndTime();
						participantList.get(word.getOwner()).addSentence(new Sentence(sentence, firstWordTime, lastWordTime, prevOwner));
					}
					
				}
				
				prevOwner = word.getOwner();
			}
			
		}
		
	}
	
	// Finner ordet med lavest meanTime og som ikke allerede er tatt ibruk
	private Word getNextWord(ArrayList<Participant> participantWords) {
	    float minValue = 999999;
		String nextWordString = "";
		float nextWordStart = 0;
		float nextWordEnd = 0;
		int owner = 0;
		
		// Blar gjennom alle ord for alle medlemmer, sammenligner tidene
		for (int i = 0; i < participantWords.size(); i++) { 
			
			for (int y = 0; y < participantWords.get(i).getWords().size(); y++) { 
				
				//Finner tidligste ord og sjekker om meanTime er brukt opp
				if (participantWords.get(i).getWords().get(y).getMeanTime() < minValue && 
						validWord(usedWords, participantWords.get(i).getWords().get(y).getMeanTime()) == true) { 
					
					minValue = participantWords.get(i).getWords().get(y).getMeanTime();
					nextWordString = participantWords.get(i).getWords().get(y).getWord();	
					nextWordStart = participantWords.get(i).getWords().get(y).getStartTime();
					nextWordEnd = participantWords.get(i).getWords().get(y).getEndTime();
					owner = participantWords.get(i).getWords().get(y).getOwner();
				}
				
			}
			
		}
		
		Word nextWord = new Word(nextWordString, nextWordStart, nextWordEnd, owner);
		return nextWord;
	}
	
	// Sjekker om meanTimen den fant allerede er tatt i bruk
	private boolean validWord(ArrayList<Float> usedWords, float wordTime) {
		
		for (int i = 0; i < usedWords.size(); i++) {
			
			if (usedWords.get(i) == wordTime) {
				return false;
			}
			
		}	
		
		return true;
	}
	
	// Genererer metadata utifra medlemmene sine data
	public void generateMetadata() {
		
	}
	
	public ArrayList<Participant> getParticipantData() {
		return participantList;
	}
	
}
