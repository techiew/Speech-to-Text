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
	
	ArrayList<wordBank> wordbank = new ArrayList<wordBank>();
	
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

			List<SpeechRecognitionResult> results = response.get().getResultsList();
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
			        
			        wordbank.add(new wordBank(word, timeStampStart, timeStampEnd));
			       // System.out.println(wordbank.get(i).getWord() + wordbank.get(i).getStartTime() + wordbank.get(i).getEndTime());
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
		//Hver lydfil har sin egen liste. Ord og timestamps blir satt kronologisk inn i listen, så vi vet at #0 i listen kommer før #1, derfor må vi bare sammenligne liste1.get(0) med liste2.get(0) og
		//se hvem som er først : )
		System.out.println(wordbank.size());
		System.out.println("jeg håper det står Dette: " + wordbank.get(0).getWord());
		System.out.println("jeg håper det står en: " + wordbank.get(2).getWord());
		System.out.println("jeg håper det står nå " + wordbank.get(12).getWord());
	}
	
}
