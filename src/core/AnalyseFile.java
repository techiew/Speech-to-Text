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

public class AnalyseFile {
	
	/**
	 * Performs non-blocking speech recognition on remote FLAC file and prints
	 * the transcription.
	 *
	 * @param gcsUri
	 *            the path to the remote LINEAR16 audio file to transcribe.
	 */
	
	private ArrayList<Participant> participantList = new ArrayList<Participant>();
	private ArrayList<Float> usedWords = new ArrayList<Float>();
	private int participantChecker = 0;
	
	public void analyseSoundFile(String gcsUri) {
		participantList.add(new Participant());
		
		// Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
		try (SpeechClient speech = SpeechClient.create()) {
			
			// Configure remote file request for Linear16
			RecognitionConfig config = RecognitionConfig.newBuilder()
					.setEncoding(AudioEncoding.LINEAR16)
					.setLanguageCode("NO")
					.setEnableWordTimeOffsets(true)
					.build();
			
			RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
			
			// Use non-blocking call for getting file transcription
			OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response = speech
					.longRunningRecognizeAsync(config, audio);
			
			while (!response.isDone()) {
				System.out.println("Venter på respons fra Google...");
				Thread.sleep(10000);
			}

			List<SpeechRecognitionResult> results = response.get().getResultsList();
			
			for (SpeechRecognitionResult result : results) {
				// There can be several alternative transcripts for a given
				// chunk of speech. Just use the first (most likely) one here.
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				System.out.printf("Transcription: %s\n", alternative.getTranscript());
				
				for (WordInfo wordInfo : alternative.getWordsList()) {
					//finner ordet
					String word = wordInfo.getWord();
					
					// finner start tiden
					float startNanosecond = (wordInfo.getStartTime().getNanos() / 100000000);
					float startSecond = wordInfo.getStartTime().getSeconds();
					float timeStampStart = startSecond + (startNanosecond / 10);
					
					//finner slutt tiden
					float endNanosecond = (wordInfo.getEndTime().getNanos() / 100000000);
					float endSecond = wordInfo.getEndTime().getSeconds();
					float timeStampEnd = endSecond + (endNanosecond / 10);
					
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void constructSentences() {		
		int prevOwner = -1;
		ArrayList<Float> temporaryStorage = new ArrayList<Float>();
		String sentence = "";
		int totalWords = 0;
		int wordCount = 0;
		
		for(int i = 0; i < participantList.size(); i++) {
			totalWords += participantList.get(i).getWords().size();
		}
		
		for (int z = 0; z < participantList.size(); z++) {

			for (int x = 0; x < participantList.get(z).getWords().size(); x++) {
				Word word = getNextWord(participantList);
				
				if (prevOwner == word.getOwner()) {
					sentence = sentence + " " + word.getWord();
					temporaryStorage.add(word.getStartTime());
					usedWords.add(word.getMeanTime());
				} else {
					
					if (sentence != "") {
						if(sentence.charAt(0) == ' ') sentence = sentence.substring(1);
						participantList.get(prevOwner).addSentence(new Sentence(sentence, temporaryStorage.get(0), word.getEndTime(), prevOwner));
						sentence = "";
						temporaryStorage.clear();
					}
						
					sentence = sentence + " " + word.getWord();
					temporaryStorage.add(word.getStartTime());
					usedWords.add(word.getMeanTime());
				}
				
				wordCount++;
				
				if(wordCount == totalWords) {
					if(sentence.charAt(0) == ' ') sentence = sentence.substring(1);
					participantList.get(prevOwner).addSentence(new Sentence(sentence, temporaryStorage.get(0), word.getEndTime(), prevOwner));
				}
				
				prevOwner = word.getOwner();
			}
			
		}
		
	}
	
	//Metode som finner ordet med lavest meanTime og som ikke allerede er tatt ibruk. 
	private Word getNextWord(ArrayList<Participant> participantWords) {
	    float minValue = 60000;
		String nextWordString = "";
		float nextWordStart = 0;
		float nextWordEnd = 0;
		int owner = 0;
		
		//Blar gjennom hele arrayet
		for (int i = 0; i < participantWords.size(); i++) { 
			
			//Blar gjennom hele lista
			for (int y = 0; y < participantWords.get(i).getWords().size(); y++) { 
				
				if (participantWords.get(i).getWords().get(y).getMeanTime() < minValue && 
						validWord(usedWords, participantWords.get(i).getWords().get(y).getMeanTime()) == true) { //Finner minste og sjekker om meanTime er brukt opp
					
					minValue = participantWords.get(i).getWords().get(y).getMeanTime(); //Setter lavere minvalue for neste sjekk
					nextWordString = participantWords.get(i).getWords().get(y).getWord();	
					nextWordStart = participantWords.get(i).getWords().get(y).getStartTime();
					nextWordEnd = participantWords.get(i).getWords().get(y).getEndTime();
					owner = participantWords.get(i).getWords().get(y).getOwner();
					//System.out.println("Akkurat nå er I: " + i + "og Y er: " + y);
				}
				
			}
			
		}
		
		Word nextWord = new Word(nextWordString, nextWordStart, nextWordEnd, owner);
		return nextWord;
	}
	
	//Metode som sjekker om meanTimen den fant allerede er tatt i bruk
	private boolean validWord(ArrayList<Float> usedWords, float wordTime) {
		
		for (int i = 0; i < usedWords.size(); i++) {
			
			if (usedWords.get(i) == wordTime) {
				return false;
			}
			
		}	
		
		return true;
	}
	
	public ArrayList<Participant> getParticipantData() {
		return participantList;
	}
	
	public void generateMetadata() {
		
	}
	
}
