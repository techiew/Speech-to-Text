package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

// Klasse som laster opp lydfiler til Google sine servere
// slik at vi kan bruke dems Speech-to-Text API
public class UploadFile {

	private byte[] selectedSoundFileBytes;
	private String selectedSoundFileName;
	
	public String uploadFile(String filePath) {
		initSoundFile(filePath);
		String gcsUri = uploadFileToGoogleCloud(selectedSoundFileBytes, selectedSoundFileName);
		return gcsUri;
	}
	
	// Finn filen og les dataene til den
	private void initSoundFile(String filePath) {
		File f = new File(filePath);
		
		byte[] soundFileByteArray = new byte[(int) f.length()];
		FileInputStream fis;
		
		try {
			fis = new FileInputStream(f);
			fis.read(soundFileByteArray);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Laster opp fil: " + f.getName());
		
		selectedSoundFileBytes = soundFileByteArray;
		selectedSoundFileName = f.getName();
	}
	
	// Last opp filen og få tak i dens "gcsUri", som vi bruker
	// til å peke til filene på serverne dems
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
