package core;

public class Sentence {

	private String sentence;
	private float startTime;
	private float endTime;
	
	public Sentence(String sentence, float startTime, float endTime) {
		this.sentence = sentence;
		this.startTime = startTime;
		this.endTime = endTime;	
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
	
}