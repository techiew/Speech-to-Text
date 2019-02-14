package core;

import gui.Gui;

public class Main {

	public static void main(String[] args) throws Exception {
		
		if(args.length > 0) {
			new Thread(new UploadFile(args[0])).start();
		} else {
			Gui app = new Gui();
			app.startApplication();
		}
		
	}
	
}
