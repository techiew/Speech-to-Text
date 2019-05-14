package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import gui.Gui;
import json.JsonWriter;

//koden må kunne kjøres flere ganger uten at det skjærer seg, så vi må passe på at det er mulig

public class SpeechToText {

	private Gui gui = new Gui();
	private Chat chat = new Chat();
	private ArrayList<File> guiSelectedFiles;
	private boolean writeToJsonWhenDone = false;
	
	public void startProcess() {
		//Vi gjør prosesseringen i en egen tråd for å ikke fryse GUI'en
		Thread processing = new Thread(new ProcessingThread(this));
		processing.setDaemon(true);
		//processing.start();
		gui.showChatResults("C:\\Users\\Marius\\Documents\\Programming\\Github repositories\\speech-to-text-itx\\chatdata\\chat_11.05.2019-215706.json");
	}
	
	//Callback function 
	public void onProcessingDone(ArrayList<Participant> participantList) {
		chat.setParticipants(participantList); 
		
		if(writeToJsonWhenDone) {
			System.out.println("Skriver ut transkripsjonen til .json fil...");
			JsonWriter json = new JsonWriter(this.chat);
			json.writeToJson();
			gui.showChatResults(json.getLastWrittenFilePath());
		}
		
	}
	
	
	//For å bruke programmet med brukergrensesnittet
	public void showGui() {
		writeToJsonWhenDone = true;
		gui.startApplication(this);
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
	
	public void setWriteToJsonWhenDone(boolean writeJson) {
		this.writeToJsonWhenDone = writeJson;
	}
	
}
