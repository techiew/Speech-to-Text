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
		//Vi gj�r prosesseringen i en egen tr�d for � ikke fryse GUI'en
		new Thread(new ProcessingThread(this)).start();
	}
	
	//Callback function 
	public void onProcessingDone(Chat chat) {
		this.chat = chat;
		System.out.println("Jeg klarte det!!! --------------------------------------");
		
		ArrayList<Participant> participants = chat.getParticipants();
		ArrayList<Integer> currentSentence = new ArrayList<Integer>();
		
		int totalNumOfSentences = 0;
		
		for(int i = 0; i < participants.size(); i++) {
			totalNumOfSentences += participants.get(i).getSentences().size();
		}
		
		for(int x = 0; x < totalNumOfSentences; x++) {
			Sentence earliestSentence = new Sentence("", 0.0f, 0.0f);
			int owner = -1;
			
			for(int y = 0; y < participants.size(); y++) {
				
				if(participants.get(y).getSentences().get(currentSentence.get(y)).getStartTime() <= earliestSentence.getStartTime()) {
					earliestSentence = participants.get(y).getSentences().get(y);
					owner = y;
					currentSentence.set(y, currentSentence.get(y) + 1);
				}
				
			}
			
			System.out.println("Participant " + owner + ":");
			System.out.println(earliestSentence.getStartTime() + " - " + earliestSentence.getEndTime() + " | " + earliestSentence.getSentence());
		}
		
	}
	
	//For � bruke programmet med brukergrensesnittet
	public void showGui() {
		app.startApplication(this);
	}
	
	/* Start prosessen direkte med argumenter som er sendt inn
	 * med f.eks. kommandolinja. Argumentene er pathen til hver enkelt fil, f.eks:
	 * java speechtotext "C:\fil_eksempel.wav" "C:\mappe\andre_filen.wav" osv...
	 * TODO - husk � ha st�tte for flere filer, kan mekkes med for loop p� args, pass p� at kommandoen over faktisk fungerer
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
