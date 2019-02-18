package core;

public class ProcessingThread implements Runnable {

	private SpeechToText stt;
	
	public ProcessingThread(SpeechToText stt) {
		this.stt = stt;
	}
	
	@Override
	public void run() {
		UploadFile uf = new UploadFile();
		AnalyseFile af = new AnalyseFile();
		
		String gcsUri = uf.uploadFile(stt.getGuiSelectedFiles().get(0).getAbsolutePath());
		
		if(gcsUri.length() > 0) {
			af.analyseSoundFile(gcsUri);
		}
		
		stt.onProcessingDone();
	}

}
