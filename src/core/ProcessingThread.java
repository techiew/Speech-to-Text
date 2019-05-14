package core;

import java.io.File;
import java.util.ArrayList;

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
		
		stt.onProcessingDone(participantList);
	}

}
