package core;

import java.io.File;
import java.util.ArrayList;

// Denne tråden gjør det meste av arbeidet, vi bruker en tråd fordi
// vi bruker en GUI og vi vil ikke at grensesnittet skal stoppe opp.
// For ITX sin versjon av denne koden så er det ingen GUI
// (det har dem ikke spurt om og det ville vært unødvendig kode)
// men vi beholder fortsatt tråden.
public class ProcessingThread implements Runnable {

	private SpeechToText stt;
	private ArrayList<Participant> participantList;
	
	public ProcessingThread(SpeechToText stt) {
		this.stt = stt;
	}
	
	@Override
	public void run() {
		UploadFile uf = new UploadFile();
		AnalyseFile af = new AnalyseFile();
		ArrayList<File> selectedFiles = stt.getGuiSelectedFiles();
		
		for(int i = 0; i < selectedFiles.size(); i++) {
			String gcsUri = uf.uploadFile(selectedFiles.get(i).getAbsolutePath());
		
			if(gcsUri.length() > 0) {
				System.out.println("Analyserer filen...");
				af.analyseSoundFile(gcsUri);
				System.out.println("Analyse " + (i+1) + " av " + selectedFiles.size() + " ferdig");
				System.out.println();
			} else {
				System.out.println("Error: Hoppet over fil nummer " + i + " i analysen, feil med gcsUri");
			}
		
		}
		
		af.constructSentences();
		participantList = af.getParticipantData();
		
//		for(int x = 0; x < participantList.size(); x++) {
//			
//			for(int y = 0; y < participantList.get(x).getSentences().size(); y++) {
//				System.out.println(participantList.get(x).getSentences().get(y).getSentence());
//			}
//			
//		}
		
		stt.onProcessingDone(participantList);
	}

}
