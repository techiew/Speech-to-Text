package core;

import java.util.ArrayList;

// Datastruktur som inneholder alle samtalemedlemmer
public class Chat {

	private ArrayList<Participant> participants;
	
	public void setParticipants(ArrayList<Participant> p) {
		this.participants = p;
	}
	
	public ArrayList<Participant> getParticipants() {
		return participants;
	}
	
}
