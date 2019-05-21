package json;

import core.Chat;
import core.Participant;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

// Klasse for å skrive ut strenger til json filer
// Her tar vi i bruk json-simple biblioteket for enkel json-behandling
public class JsonWriter {
	private JSONObject chat = null;
	private String lastWrittenFile = null;
	
	@SuppressWarnings("unchecked")
	public JsonWriter(Chat chat) {
		JSONObject obj = new JSONObject();
		obj.put("participants", new JSONArray());
		ArrayList<Participant> participants = chat.getParticipants();
		
		for(int x = 0; x < participants.size(); x++) {
			((JSONArray)obj.get("participants")).add(new JSONObject());
			JSONObject currParticipant = (JSONObject) ((JSONArray)obj.get("participants")).get(x);
			
			JSONObject metadata = new JSONObject();
			metadata.put("numWordsSpoken", participants.get(x).getNumWordsSpoken());
			
			JSONArray sentences = new JSONArray();
			
			for(int y = 0; y < participants.get(x).getSentences().size(); y++) {
				JSONObject sentenceObj = new JSONObject();
				sentenceObj.put("text", participants.get(x).getSentences().get(y).getSentence());
				sentenceObj.put("startTime", participants.get(x).getSentences().get(y).getStartTime());
				sentenceObj.put("endTime", participants.get(x).getSentences().get(y).getEndTime());
				sentences.add(sentenceObj);
			}
			
			currParticipant.put("metadata", metadata);
			currParticipant.put("sentences", sentences);
		}
		
		this.chat = obj;
	}
	
	public boolean writeToJson() {
		
		if(chat == null) {
			return false;
		}
		
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy-HHmmss");
			LocalDateTime now = LocalDateTime.now();
			String fileName = "chatdata/chat_" +  dtf.format(now) + ".json";
			System.out.println(fileName);
			File file = new File(fileName);
			file.getParentFile().mkdirs();
			lastWrittenFile = file.getCanonicalPath();
			FileWriter fw = new FileWriter(file);
			fw.write(chat.toJSONString());
			fw.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		
		return true;
	}
	
	public String getLastWrittenFilePath() {
		return lastWrittenFile;
	}
	
}
