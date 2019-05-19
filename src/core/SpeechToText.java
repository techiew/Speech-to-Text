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
	private boolean usingGui = false;
	
	// Start arbeidet på filene
	public void startProcess() {
		Thread processing = new Thread(new ProcessingThread(this));
		processing.setDaemon(true);
		processing.start();
		//gui.showChatResults("C:\\Users\\Marius\\Documents\\Programming\\Github repositories\\speech-to-text-itx\\chatdata\\chat_11.05.2019-215706.json");
	}
	
	// Callback funksjon, sier ifra når jobben er gjort
	public void onProcessingDone(ArrayList<Participant> participantList) {
		chat.setParticipants(participantList); 
		
		if(usingGui) {
			System.out.println("Skriver ut transkripsjonen til en .json fil...");
			JsonWriter json = new JsonWriter(this.chat);
			json.writeToJson();
			gui.showChatResults(json.getLastWrittenFilePath());
		}
		
	}
	
	// Viser brukergrensesnittet, for å velge filer
	// Vi må også skrive resultatene til en json fil når vi bruker GUI,
	// fordi GUI'en leser fra en json fil
	public void showGui() {
		usingGui = true;
		gui.startApplication(this);
	}
	
	// Grensesnitt for ITX sin kode
	public Chat phoneConversationToChat(List<File> wavFiles) {
		startProcess();
		return chat;
	}

	public ArrayList<File> getGuiSelectedFiles() {
		return guiSelectedFiles;
	}

	public void setGuiSelectedFiles(List<File> guiSelectedFiles) {
		this.guiSelectedFiles = new ArrayList<File>(guiSelectedFiles);
	}
	
}
