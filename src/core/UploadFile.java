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

public class UploadFile {

	private byte[] selectedSoundFileBytes;
	private String selectedSoundFileName;
	
	public String uploadFile(String filePath) {
		initSoundFile(filePath);
		String gcsUri = uploadFileToGoogleCloud(selectedSoundFileBytes, selectedSoundFileName);
		return gcsUri;
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
	
	private String uploadFileToGoogleCloud(byte[] soundFileByteArray, String filename) {
		
		Storage storage = StorageOptions.getDefaultInstance().getService();
		BlobId blobId = BlobId.of("goodest_team_lydfiler", filename);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		Blob blob = storage.create(blobInfo,soundFileByteArray); 
		String gcsUri = "gs://goodest_team_lydfiler/" + filename;
		System.out.println("Filen er lastet opp under: " + gcsUri);
		
		return gcsUri;
	}
	
}
