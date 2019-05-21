package core;

import java.io.File;
import java.util.ArrayList;

// Denne tr�den gj�r det meste av arbeidet, vi bruker en tr�d fordi
// vi bruker en GUI og vi vil ikke at grensesnittet skal stoppe opp.
// For ITX sin versjon av denne koden s� er det ingen GUI
// (det har dem ikke spurt om og det ville v�rt un�dvendig kode)
// men vi beholder fortsatt tr�den.
public class ProcessingThread implements Runnable {

	private SpeechToText stt;
	private ArrayList<Participant> participantList;
	private ArrayList<File> selectedFiles;
	
	public ProcessingThread(SpeechToText stt, ArrayList<File> selectedFiles) {
		this.stt = stt;
		this.selectedFiles = selectedFiles;
	}
	
	@Override
	public void run() {
		UploadFile uf = new UploadFile();
		AnalyseFile af = new AnalyseFile();
		String fileToBlame = "";
		
		for(int i = 0; i < selectedFiles.size(); i++) {
			stt.giveFeedbackToGui("Laster opp fil " + (i + 1) + " av " + selectedFiles.size() + "...");
			String gcsUri = uf.uploadFile(selectedFiles.get(i).getAbsolutePath());
		
			if(gcsUri.length() > 0) {
				stt.giveFeedbackToGui("Analyserer fil " + (i + 1) + " av " + selectedFiles.size() + "...");
				System.out.println("Analyserer filen...");
				
				if(!af.analyseSoundFile(gcsUri)) {
					fileToBlame = selectedFiles.get(i).getName();
					System.out.println("Feil med fil \"" + fileToBlame + "\" i analysen, sjekk filkriterier");
					stt.onProcessingError(fileToBlame);
					return;
				}
				
				System.out.println("Analyse " + (i + 1) + " av " + selectedFiles.size() + " ferdig");
				System.out.println();
			} else {
				fileToBlame = selectedFiles.get(i).getName();
				System.out.println("Feil med fil \"" + fileToBlame + "\" i analysen, feil med gcsUri");
				stt.onProcessingError(fileToBlame);
				return;
			}
		
		}
		
		af.constructSentences();
		af.generateMetadata();
		participantList = af.getParticipantData();
		
		stt.onProcessingDone(participantList);
	}

}
