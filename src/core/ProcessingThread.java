package core;

import java.io.File;
import java.util.ArrayList;

public class ProcessingThread implements Runnable {

	private SpeechToText stt;
	private ArrayList<Participant> participantList;
	private ArrayList<Float> usedSentences = new ArrayList<Float>();
	
	public ProcessingThread(SpeechToText stt) {
		this.stt = stt;
	}
	
	@Override
	public void run() {
		Chat chat = new Chat();
		UploadFile uf = new UploadFile();
		AnalyseFile af = new AnalyseFile();
		ArrayList<File> selectedFiles = stt.getGuiSelectedFiles();
		System.out.println("antall filer valgt:" + selectedFiles.size());
		
		for(int i = 0; i < selectedFiles.size(); i++) {
			String gcsUri = uf.uploadFile(selectedFiles.get(i).getAbsolutePath());
		
			if(gcsUri.length() > 0) {
				af.analyseSoundFile(gcsUri);
			} else {
				System.out.println("Error: Hoppet over fil nummer " + i + " i analysen, feil med gcsUri");
			}
		
		}
		
		af.constructSentences();
		participantList = af.getParticipantData();
		
		Sentence currentSentence = null;
		for (int i = 0; i < participantList.size(); i++) {
			
			for (int y = 0; y < participantList.get(i).getSentences().size(); y++) {
				currentSentence = getNextSentence(participantList);
				System.out.println(currentSentence.getSentence());							//Han her skriver ut, istedet for å skrive ut kan han skrive til JSON fil eller noe. 
				usedSentences.add(currentSentence.getMeanTime());
			}
			
		}
		//chat.addParticipant(af.getParticipantData(i));
		stt.onProcessingDone(participantList);
		
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

}
