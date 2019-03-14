package core;

public class Sentence {

	private String sentence;
	private float startTime;
	private float endTime;
	private float owner;
	
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