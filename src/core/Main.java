package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		SpeechToText stt = new SpeechToText();
		stt.showGui();
		
		// Under ligger et eksempel av bruk på ITX sitt grensesnitt
		// som de ønsket at vi skulle ha i koden vår
		//List<File> wavFiles = new ArrayList<File>();
		//wavFiles.add(new File("lydfil1.wav"));
		//wavFiles.add(new File("lydfil2.wav"));
		//Chat chat = stt.phoneConversationToChat(wavFiles);
		//printChatToConsole(chat);
	}
	
	private static void printChatToConsole(Chat chat) {
		
		for(int i = 0; i < chat.getParticipants().size(); i++) {
			
			for(int j = 0; j < chat.getParticipants().get(i).getSentences().size(); j++) {
				System.out.println(chat.getParticipants().get(i).getSentences().get(j).getSentence());
			}
			
		}
		
	}
	
}
