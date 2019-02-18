package core;

public class Word {

	private String word;
	private float startTime;
	private float endTime;
	
	public Word(String word, float startTime, float endTime) {
		this.word = word;
		this.startTime = startTime;
		this.endTime = endTime;	
	}
	
	public String getWord() {
		return word;
	}
	
	public float getStartTime() {
		return startTime;
	}
	
	public float getEndTime() {
		return endTime;
	}
	
}
