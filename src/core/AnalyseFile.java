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
	
	private List<SpeechRecognitionResult> results;
	private ArrayList<Word> wordParticipant1 = new ArrayList<Word>();
	private ArrayList<Word> wordParticipant2 = new ArrayList<Word>();
	private ArrayList<Sentence> sentenceParticipant1 = new ArrayList<Sentence>();
	private ArrayList<Sentence> sentenceParticipant2 = new ArrayList<Sentence>();
	static int numberOfParticipants = 1;
	
	public void analyseSoundFile(String gcsUri) {
		
		// Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
		try (SpeechClient speech = SpeechClient.create()) {
			
			// Configure remote file request for Linear16
			RecognitionConfig config = RecognitionConfig.newBuilder()
					.setEncoding(AudioEncoding.LINEAR16)
					.setLanguageCode("NO")
					.setSampleRateHertz(44100)
					.setEnableWordTimeOffsets(true)
					.build();
			
			RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
			
			// Use non-blocking call for getting file transcription
			OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response = speech
					.longRunningRecognizeAsync(config, audio);
			
			while (!response.isDone()) {
				System.out.println("Venter på respons...");
				Thread.sleep(10000);
			}

			results = response.get().getResultsList();
			System.out.println("Svar:");
			
			for (SpeechRecognitionResult result : results) {
				// There can be several alternative transcripts for a given
				// chunk of speech. Just use the
				// first (most likely) one here.
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
					float endSecond = wordInfo.getStartTime().getSeconds();
					float timeStampEnd = endSecond + (endNanosecond / 10);
					
					if (numberOfParticipants == 1)
					{
						wordParticipant1.add(new Word(word, timeStampStart, timeStampEnd));
					}
					else if (numberOfParticipants == 2)
					{
						wordParticipant2.add(new Word(word, timeStampStart, timeStampEnd));
					}
					numberOfParticipants++;
					//System.out.println(wordbank.get(i).getWord() + wordbank.get(i).getStartTime() + wordbank.get(i).getEndTime());
				}
				
			}
			
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
	
	public void parseOutput() {
		//Variabler for å bla gjennom listene
		int i = 0;
		int y = 0;
		//Setningen stringen som sendes brukes i parameter av instansiering for Sentence
		String sentence = null;
		//Midlertidig lagringsplass for startTiden til det første ordet. Brukes for å markere starten av setningen
		List<Float> temporaryStorage = new ArrayList<Float>();
		while (wordParticipant1.size() > i && wordParticipant2.size() > y)													   //Looper så lenge listene fortsatt har ord i seg
		{
			
			while (wordParticipant1.get(i).getEndTime() > wordParticipant2.get(y).getStartTime())							   //Sjekk om at ord X ikke overrider med noen ord fra andre personen
			{
				temporaryStorage.add(wordParticipant1.get(i).getStartTime());												   //Adder startTiden i listen. Bare interessert i nummer 0
				sentence = sentence + wordParticipant1.get(i);                                                                 //Setter ordet i en string
				i++;
			}
			sentenceParticipant1.add(new Sentence(sentence, temporaryStorage.get(0), wordParticipant1.get(i).getEndTime()));   //Instansierer ny sentence objekt inn i listen
			temporaryStorage.clear();																						   //Clearer midlertidig lagringsrommet. Sentence allerede lagd
			sentence = "";																									   //Clearer setningen. Sentence allerede lagd
			
			while (wordParticipant2.get(y).getEndTime() > wordParticipant1.get(i).getStartTime())
			{
				temporaryStorage.add(wordParticipant2.get(y).getStartTime());
				sentence = sentence + wordParticipant2.get(y);
				y++;
			}
			sentenceParticipant2.add(new Sentence(sentence, temporaryStorage.get(0), wordParticipant1.get(y).getEndTime()));
			temporaryStorage.clear();
			sentence = "";
			
		}
		
		
		//Hver lydfil har sin egen liste. Ord og timestamps blir satt kronologisk inn i listen, så vi vet at #0 i listen kommer før #1, derfor må vi bare sammenligne liste1.get(0) med liste2.get(0) og
		//se hvem som er først : )
		/*System.out.println(wordParticipant1.size());
		System.out.println("jeg håper det står Dette: " + wordParticipant1.get(0).getWord());
		System.out.println("jeg håper det står en: " + wordParticipant1.get(2).getWord());
		System.out.println("jeg håper det står nå " + wordParticipant1.get(12).getWord()); */
	}
	
}
