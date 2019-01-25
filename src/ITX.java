import java.util.List;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1p1beta1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1p1beta1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;


public class ITX {

	/**
	 * Performs non-blocking speech recognition on remote FLAC file and prints the transcription.
	 *
	 * @param gcsUri the path to the remote LINEAR16 audio file to transcribe.
	 */
	public ITX(String gcsUri) throws Exception {
	  // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
	  try (SpeechClient speech = SpeechClient.create()) {
	    // Configure remote file request for Linear16
	    RecognitionConfig config =
	        RecognitionConfig.newBuilder()
	            .setEncoding(AudioEncoding.FLAC)
	            .setLanguageCode("NO")
	            .setSampleRateHertz(16000)
	            .build();
	    RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();
	    // Use non-blocking call for getting file transcription
	    OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
	        speech.longRunningRecognizeAsync(config, audio);
	    while (!response.isDone()) {
	      System.out.println("Waiting for response...");
	      Thread.sleep(10000);
	    }

	    List<SpeechRecognitionResult> results = response.get().getResultsList();
	    System.out.println("Svar:");
	    for (SpeechRecognitionResult result : results) {
	      // There can be several alternative transcripts for a given chunk of speech. Just use the
	      // first (most likely) one here.
	      SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
	      System.out.printf("Transcription: %s\n", alternative.getTranscript());
	    }
	  }
	}
}
