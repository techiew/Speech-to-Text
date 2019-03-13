package core;

import java.util.ArrayList;

public class Chat {

	private ArrayList<Participant> participants;
	
	public void addParticipant(Participant p) {
		this.participants.add(p);
	}
	
	public ArrayList<Participant> getParticipants() {
		return participants;
	}
	
}
