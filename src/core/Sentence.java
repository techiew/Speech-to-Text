package core;

public class Sentence {

	private String sentence;
	private float startTime;
	private float endTime;
	private float owner;
	
	//Setninger består av grupper med ord som varer til et annet medlem i samtalen begynner å prate
	public Sentence(String sentence, float startTime, float endTime, float owner) {
		this.sentence = sentence;
		this.startTime = startTime;
		this.endTime = endTime;	
		this.owner = owner;
	}
	
	public String getSentence() {
		return sentence;
	}
	
	public float getStartTime() {
		return startTime;
	}
	
	public float getEndTime() {
		return endTime;
	}
	
	public float getMeanTime() {
		return (startTime + endTime) / 2;
	}
	
	public float getOwner() {
		return owner;
	}
	
}