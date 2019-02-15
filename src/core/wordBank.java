package core;

public class wordBank {

	private  String word;
	private  float startTime;
	private  float endTime;
	
	public wordBank(String wordPara, float start, float end) {
		word = wordPara;
		startTime = start;
		endTime = end;	
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
