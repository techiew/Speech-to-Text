package gui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import core.Participant;
import core.Sentence;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ResultsController {

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private GridPane chatGridPane;

	private ArrayList<Float> usedSentences = new ArrayList<Float>();
	
	private int chatHeight = 1;
	
	// Lag den Messenger-aktige strukturen i samtalevisningen
    public void createChatLog(String fileToRead) {
    	JSONParser parser = new JSONParser();
    	ArrayList<Participant> participantList = new ArrayList<Participant>();
    	
    	try(FileReader reader = new FileReader(fileToRead)) {
    		Object obj = parser.parse(reader);
    		JSONObject chatobj = (JSONObject) obj;
    		JSONArray participants = (JSONArray) chatobj.get("participants");
    		
    		for(int i = 0; i < participants.size(); i++) {
    			Participant participant = new Participant();
    			int numSentences = ((JSONArray)((JSONObject) participants.get(i)).get("sentences")).size();
    			
    			for(int j = 0; j < numSentences; j++) {
    				String text = (String) ((JSONObject)((JSONArray)((JSONObject) participants.get(i)).get("sentences")).get(j)).get("text");
    				float startTime = ((Double) ((JSONObject)((JSONArray)((JSONObject) participants.get(i)).get("sentences")).get(j)).get("startTime")).floatValue();
    				float endTime = ((Double) ((JSONObject)((JSONArray)((JSONObject) participants.get(i)).get("sentences")).get(j)).get("endTime")).floatValue();
    				Sentence sentence = new Sentence(text, startTime, endTime, (float)i);
    				participant.addSentence(sentence);
    			}
    			
				participantList.add(participant);
    		}
    		
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Sentence currentSentence = null;
		int prevOwner = -1;
		int col = 0;
		for (int i = 0; i < participantList.size(); i++) {
			
			for (int y = 0; y < participantList.get(i).getSentences().size(); y++) {
				currentSentence = getNextSentence(participantList);
				SentenceBubble bubble;
				
				bubble = new SentenceBubble(currentSentence.getSentence(), 0, (int)currentSentence.getOwner());
				
				if(prevOwner == -1) {
					prevOwner = (int)currentSentence.getOwner();
				} else if(prevOwner != currentSentence.getOwner()) {
					
					if(col == 0) {
						col = 1;
					} else {
						col = 0;
					}
					
				}
				
				prevOwner = (int)currentSentence.getOwner();
				
				SentenceBubble participantNumber = new SentenceBubble("Deltaker " + (((int)currentSentence.getOwner()) + 1), 1, 0);
				participantNumber.setEditable(false);
				chatGridPane.add(participantNumber, col, chatHeight);
				chatGridPane.setMargin(participantNumber, new Insets(0, 15, -20, 10));
				
				bubble.setEditable(false);
				chatGridPane.add(bubble, col, chatHeight + 1);
				chatGridPane.setMargin(bubble, new Insets(5, 15, 5, 15));
				
				SentenceBubble timestamp = new SentenceBubble("~" + currentSentence.getMeanTime() + "s", 1, 0);
				timestamp.setEditable(false);
				chatGridPane.add(timestamp, col, chatHeight + 2);
				chatGridPane.setMargin(timestamp, new Insets(-5, 15, 0, 15));
				
				usedSentences.add(currentSentence.getMeanTime());
				
				chatHeight += 3;
			}
			
		}
		
    }
    
    // Få neste, tidligste setningen i setninglisten
	private Sentence getNextSentence(ArrayList<Participant> participantSentences) {
		float minValue = 999999;
		Sentence sentence = new Sentence("ERROR", 0.0f, 0.0f, 0);
		
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
	
	// Har vi brukt denne setningen før? i så fall hopper vi over den
	private boolean validSentence(ArrayList<Float> usedSentences, float sentenceMeanTime) {
		
		for (int i = 0; i < usedSentences.size(); i++) {
			
			if (usedSentences.get(i) == sentenceMeanTime) {
				return false;
			}
			
		}	
		
		return true;
	}
	
}

