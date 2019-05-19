package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import gui.Gui;
import json.JsonWriter;

//koden m� kunne kj�res flere ganger uten at det skj�rer seg, s� vi m� passe p� at det er mulig

public class SpeechToText {

	private Gui gui = new Gui();
	private Chat chat = new Chat();
	private ArrayList<File> guiSelectedFiles;
	private boolean usingGui = false;
	
	// Start arbeidet p� filene
	public void startProcess() {
		Thread processing = new Thread(new ProcessingThread(this));
		processing.setDaemon(true);
		processing.start();
		//gui.showChatResults("C:\\Users\\Marius\\Documents\\Programming\\Github repositories\\speech-to-text-itx\\chatdata\\chat_11.05.2019-215706.json");
	}
	
	// Callback funksjon, sier ifra n�r jobben er gjort
	public void onProcessingDone(ArrayList<Participant> participantList) {
		chat.setParticipants(participantList); 
		
		if(usingGui) {
			System.out.println("Skriver ut transkripsjonen til en .json fil...");
			JsonWriter json = new JsonWriter(this.chat);
			json.writeToJson();
			gui.showChatResults(json.getLastWrittenFilePath());
		}
		
	}
	
	// Viser brukergrensesnittet, for � velge filer
	// Vi m� ogs� skrive resultatene til en json fil n�r vi bruker GUI,
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
