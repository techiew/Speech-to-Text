package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gui.Gui;

public class SpeechToText {

	private Gui app = new Gui();
	private Chat chat = new Chat();
	private ArrayList<File> guiSelectedFiles;
	
	public void startProcess() {
		//Vi gjør prosesseringen i en egen tråd for å ikke fryse GUI'en
		new Thread(new ProcessingThread(this)).start();
	}
	
	//Callback function 
	public void onProcessingDone(Chat chat) {
		this.chat = chat;
		System.out.println("Jeg klarte det!!!");
	}
	
	//For å bruke programmet med brukergrensesnittet
	public void showGui() {
		app.startApplication(this);
	}
	
	/* Start prosessen direkte med argumenter som er sendt inn
	 * med f.eks. kommandolinja. Argumentene er pathen til hver enkelt fil, f.eks:
	 * java speechtotext "C:\fil_eksempel.wav" "C:\mappe\andre_filen.wav" osv...
	 * TODO - husk å ha støtte for flere filer, kan mekkes med for loop på args, pass på at kommandoen over faktisk fungerer
	 */
	public void runWithArgs(String[] args) {
		//String gcsUri = new Thread(new UploadFile(args[0])).start();
	}
	
	//Grensesnitt for ITX sin kode
	public Chat phoneConversationToChat(List<File> wavFiles) {
		return chat;
	}

	public ArrayList<File> getGuiSelectedFiles() {
		return guiSelectedFiles;
	}

	public void setGuiSelectedFiles(List<File> guiSelectedFiles) {
		this.guiSelectedFiles = new ArrayList<File>(guiSelectedFiles);
	}
	
}
