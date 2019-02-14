package core;

import gui.MyApplication;

public class Main {

	public static void main(String[] args) throws Exception {
		
		if(args.length > 0) {
			new Thread(new SpeechToText(args[0])).start();
		} else {
			MyApplication app = new MyApplication();
			app.startApplication();
		}
		
	}
	
}
