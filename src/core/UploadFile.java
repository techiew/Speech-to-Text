package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import ie.corballis.sox.SoXEffect;
import ie.corballis.sox.Sox;
import ie.corballis.sox.WrongParametersException;

// Klasse som laster opp lydfiler til Google sine servere
// slik at vi kan bruke dems Speech-to-Text API
public class UploadFile {

	private byte[] selectedSoundFileBytes;
	private String selectedSoundFileName;
	boolean soxWorked = true;
	private ArrayList<File> tempFilesToDelete = new ArrayList<File>();
	
	public String uploadFile(String filePath) {
		initSoundFile(filePath);
		String gcsUri = uploadFileToGoogleCloud(selectedSoundFileBytes, selectedSoundFileName);
		return gcsUri;
	}
	
	// Finn filen og les dataene til den
	private void initSoundFile(String filePath) {
		File f = modifyFileWithSox(filePath);
		
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
	
	// Her legger vi til "noise" som bakgrunnslyd i lydfilen,
	// dette må vi ha slik at vi lurer Google sin Speech-to-Text API til
	// å ikke ignorere stillhet i samtalen i forhold til timestamps som den returnerer
	private File modifyFileWithSox(String filePath) {
		soxWorked = true;
		
		File file = new File(filePath);
		File tempFile = new File("temp/temp_" + file.getName());
		tempFile.getParentFile().mkdirs();
		
		Sox sox = new Sox("bin/sox/sox.exe");
        try {
			sox
				.inputFile(filePath)
			    .outputFile("temp/temp_norm_" + file.getName())
			    .effect(SoXEffect.NORM, "")
			    .execute();
			
			sox
			 	.inputFile("temp/temp_norm_" + file.getName())
			 	.outputFile("temp/temp_synth_" + file.getName())
			 	.effect(SoXEffect.SYNTH, "whitenoise", "vol", "0.002")
			 	//.effect(SoXEffect.SYNTH, "sine", "300", "vol", "1")
				.execute();
			
			sox
				.argument("-m", "temp/temp_norm_" + file.getName(), "temp/temp_synth_" + file.getName())
				.outputFile("temp/temp_merge_" + file.getName())
				.execute();
			
		} catch (IOException | WrongParametersException e) {
			e.printStackTrace();
			soxWorked = false;
		}
    	
        if(soxWorked) {
        	tempFilesToDelete.add(new File("temp/temp_norm_" + file.getName()));
        	tempFilesToDelete.add(new File("temp/temp_synth_" + file.getName()));
        	tempFilesToDelete.add(new File("temp/temp_merge_" + file.getName()));
        	return new File("temp/temp_merge_" + file.getName());
        }
        
        return file;
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
		
		if(soxWorked) {
			
			for(int i = 0; i < tempFilesToDelete.size(); i++) {
				
				if(tempFilesToDelete.get(i).exists()) {
					tempFilesToDelete.get(i).delete();
				}
				
			}
			
		}
		
		return gcsUri;
	}
	
}
