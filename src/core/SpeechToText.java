package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gui.Gui;

public class SpeechToText {

	private Gui app = new Gui();
	private Chat chat = new Chat();
	private ArrayList<File> guiSelectedFiles;
	private ArrayList<Float> usedSentences = new ArrayList<Float>();
	
	public void startProcess() {
		//Vi gjør prosesseringen i en egen tråd for å ikke fryse GUI'en
		new Thread(new ProcessingThread(this)).start();
	}
	
	//Callback function 
	public void onProcessingDone(ArrayList<Participant> participantList) {
		System.out.println("Jeg klarte det!!! --------------------------------------");
		Sentence currentSentence = null;
		
		for (int i = 0; i < participantList.size(); i++) {
			
			for (int y = 0; y < participantList.get(i).getSentences().size(); y++) {
				currentSentence = getNextSentence(participantList);
				System.out.println(currentSentence.getSentence());
				usedSentences.add(currentSentence.getMeanTime());
			}
			
		}
		
		/*ArrayList<Participant> participants = chat.getParticipants();
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
		} */
		
	}
	
	private Sentence getNextSentence(ArrayList<Participant> participantSentences) {
		float minValue = 60000;
		Sentence sentence = null;
		
		for (int i = 0; i < participantSentences.size(); i++) {	
			
			for (int y = 0; y < participantSentences.get(i).getSentences().size(); y++) {	
				
				if (participantSentences.get(i).getSentences().get(y).getMeanTime() < minValue && 
						validSentence(usedSentences, participantSentences.get(i).getSentences().get(y).getMeanTime()) == true) {
					
					minValue = participantSentences.get(i).getSentences().get(y).getMeanTime();	
					sentence = participantSentences.get(i).getSentences().get(y);
				}
				
			}
			
		}
		
		return sentence;
	}
	
	private boolean validSentence(ArrayList<Float> usedSentences, float sentence) {
		
		for (int i = 0; i < usedSentences.size(); i++) {
			
			if (usedSentences.get(i) == sentence) {
				return false;
			}
			
		}	
		
		return true;
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
