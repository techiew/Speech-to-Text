import java.io.File;

public class GetFile {
	
	public GetFile() throws Exception {
		File f = new File("Innspilling (2).wav");	// Denne klassen kan da erstattes med GUI klassen fordi den henter bare filen. Vil BARE ha filen. Navn tas av i InitializeSoundFile.java 
		new InitializeSoundFile(f);
	}
}
