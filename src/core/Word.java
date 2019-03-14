package core;

public class Word {

	private String word;
	private float startTime;
	private float endTime;
	private int owner;
	
	public Word(String word, float startTime, float endTime, int owner) {
		this.word = word;
		this.startTime = startTime;
		this.endTime = endTime;	
		this.owner = owner;
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
	
	public int getOwner() {
		return owner;
	}
	
	public float getMeanTime() {
		return (startTime + endTime) / 2;
	}
	
}
