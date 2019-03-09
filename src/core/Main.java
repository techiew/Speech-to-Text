package core;

public class Main {

	public static void main(String[] args) {
		
		SpeechToText stt = new SpeechToText();
		
		if(args.length > 0) {
			stt.runWithArgs(args);
		} else {
			stt.showGui();
		}
		
	}
	
}
