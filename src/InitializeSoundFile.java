import java.io.File;
import java.io.FileInputStream;

public class InitializeSoundFile {

	public InitializeSoundFile(File f) throws Exception {
	    //	String gcsUri = "gs://itx_test1_test2/test2";
		
		byte[] soundFileByteArray = new byte[(int) f.length()];
		FileInputStream fis = new FileInputStream(f);
		fis.read(soundFileByteArray);
		System.out.println("Filnavnet er: " + f.getName());
		
		//new ITX(gcsUri);
		new UploadFile(soundFileByteArray, f.getName());
	}
}
