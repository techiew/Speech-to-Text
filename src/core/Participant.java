package core;

import java.util.ArrayList;

public class Participant {

	private int numWordsSpoken = 0;
	private int wordsPerMinute = 0;
	
	private ArrayList<Word> words;
	private ArrayList<Sentence> sentences;
	
	public int getNumWordsSpoken() {
		return numWordsSpoken;
	}
	public void setNumWordsSpoken(int numWordsSpoken) {
		this.numWordsSpoken = numWordsSpoken;
	}
	
	public int getWordsPerMinute() {
		return wordsPerMinute;
	}
	
	public void setWordsPerMinute(int wordsPerMinute) {
		this.wordsPerMinute = wordsPerMinute;
	}
	
	public ArrayList<Word> getWords() {
		return words;
	}
	
	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}
	
	public ArrayList<Sentence> getSentences() {
		return sentences;
	}
	
	public void setSentences(ArrayList<Sentence> sentences) {
		this.sentences = sentences;
	}
	
}
