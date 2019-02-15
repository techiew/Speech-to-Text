package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1p1beta1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1p1beta1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import core.AnalyseFile;

public class UploadFile implements Runnable {

	/**
	 * Performs non-blocking speech recognition on remote FLAC file and prints
	 * the transcription.
	 *
	 * @param gcsUri
	 *            the path to the remote LINEAR16 audio file to transcribe.
	 */
	
	private byte[] selectedSoundFileBytes;
	private String selectedSoundFileName;
	private String soundFileGcsUri;
	private String givenFilePath;
	//List<SpeechRecognitionResult> results; -- for å lagre resultatene senere
	
	private AnalyseFile af;
	
	public UploadFile(String filePath) {
		givenFilePath = filePath;
		af = new AnalyseFile();
	}
	
	@Override
	public void run()
	{
		initSoundFile(givenFilePath);
		uploadFileToGoogleCloud(selectedSoundFileBytes, selectedSoundFileName);
		af.analyseSoundFile(soundFileGcsUri);
		af.parseOutput();
	}
	
	private void initSoundFile(String filePath) {
	    //String gcsUri = "gs://itx_test1_test2/test2";
		
		File f = new File(filePath);
		
		byte[] soundFileByteArray = new byte[(int) f.length()];
		FileInputStream fis;
		
		try {
			fis = new FileInputStream(f);
			fis.read(soundFileByteArray);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Filnavnet er: " + f.getName());
		
		//new ITX(gcsUri);
		//uploadFileToGoogleCloud(soundFileByteArray, f.getName());
		selectedSoundFileBytes = soundFileByteArray;
		selectedSoundFileName = f.getName();
	}
	
	private void uploadFileToGoogleCloud(byte[] soundFileByteArray, String filename) {
		
		Storage storage = StorageOptions.getDefaultInstance().getService();
		BlobId blobId = BlobId.of("goodest_team_lydfiler", filename);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		Blob blob = storage.create(blobInfo,soundFileByteArray); 
		String gcsUri = "gs://goodest_team_lydfiler/" + filename;
		System.out.println("Filen er lastet opp under: " + gcsUri);
		
		soundFileGcsUri = gcsUri;
	}
	
}
