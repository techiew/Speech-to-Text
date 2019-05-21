package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import gui.Gui;
import javafx.application.Platform;
import json.JsonWriter;

//koden må kunne kjøres flere ganger uten at det skjærer seg, så vi må passe på at det er mulig

public class SpeechToText {

	private Gui gui = new Gui();
	private Chat chat = new Chat();
	private ArrayList<File> selectedFiles;
	private boolean usingGui = false;
	private boolean processingFinished = false;
	
	// Starter arbeidet på filene
	public void startProcess() {
		Thread processing = new Thread(new ProcessingThread(this, selectedFiles));
		processing.setDaemon(true);
		processing.start();
	}
	
	// Callback funksjon, sier ifra når jobben er ferdig i ProcessingThread
	public void onProcessingDone(ArrayList<Participant> participantList) {
		chat.setParticipants(participantList); 
		processingFinished = true;
		
		// Vi må skrive resultatene til en json fil når vi bruker GUI,
		// fordi GUI'en leser fra en json fil
		if(usingGui) {
			System.out.println("Skriver ut transkripsjonen til en .json fil...");
			JsonWriter json = new JsonWriter(this.chat);
			json.writeToJson();
			gui.showChatResults(json.getLastWrittenFilePath());
		}
		
	}
	
	public void onProcessingError(String fileToBlame) {
		System.out.println("Analyseringen ble avbrutt.");   
		
		if(!usingGui) return;
		
		// Platform.runLater får koden til å kjøre på JavaFX sin tråd
		Platform.runLater(() -> {
			gui.resetMainWindow();
			gui.showErrorMsg("Feil med fil: \"" + fileToBlame + "\", sjekk filkriterier");
		});
		
	}
	
	public void giveFeedbackToGui(String feedback) {
		if(!usingGui) return;
		
		Platform.runLater(() -> {
			gui.showFeedbackMsg(feedback);
		});
		
	}
	
	// Viser brukergrensesnittet, for å la brukeren velge filer
	public void showGui() {
		usingGui = true;
		gui.startApplication(this);
	}
	
	// Grensesnitt for ITX sin kode
	public Chat phoneConversationToChat(List<File> wavFiles) {
		selectedFiles = (ArrayList<File>) wavFiles;
		startProcess();
		
		while(!processingFinished) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		processingFinished = false;
		return chat;
	}

	public ArrayList<File> getSelectedFiles() {
		return selectedFiles;
	}

	public void setSelectedFiles(List<File> selectedFiles) {
		this.selectedFiles = new ArrayList<File>(selectedFiles);
	}
	
}
