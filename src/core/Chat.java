package core;

import java.util.ArrayList;

public class Chat {

	private ArrayList<Participant> participants;
	
	public void addParticipant(ArrayList<Participant> p) {
		this.participants = p;
	}
	
	public ArrayList<Participant> getParticipants() {
		return participants;
	}
	
}
