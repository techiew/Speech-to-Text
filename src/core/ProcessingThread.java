package core;

import java.io.File;
import java.util.ArrayList;

public class ProcessingThread implements Runnable {

	private SpeechToText stt;
	
	public ProcessingThread(SpeechToText stt) {
		this.stt = stt;
	}
	
	@Override
	public void run() {
		Chat chat = new Chat();
		System.out.println("test1");
		UploadFile uf = new UploadFile();
		System.out.println("test");
		AnalyseFile af = new AnalyseFile();
		ArrayList<File> selectedFiles = stt.getGuiSelectedFiles();
		System.out.println("antall filer valgt:" + selectedFiles.size());
		
		for(int i = 0; i < selectedFiles.size(); i++) {
			String gcsUri = uf.uploadFile(selectedFiles.get(i).getAbsolutePath());
		
			if(gcsUri.length() > 0) {
				af.analyseSoundFile(gcsUri, selectedFiles.size());
			} else {
				System.out.println("Error: Hoppet over fil nummer " + i + " i analysen, feil med gcsUri");
			}
		
		}
		af.constructSentences();
		//chat.addParticipant(af.getParticipantData(i));
		
		stt.onProcessingDone(chat);
	}

}
