package core;

import java.util.ArrayList;

// Datastruktur for en enkelt samtaledeltaker, inneholder setningene og orda dems
// Og litt metadata
public class Participant {

	//Metadata
	private int numWordsSpoken = 0;
	
	private ArrayList<Word> wordList = new ArrayList<Word>();
	private ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();
	
	public int getNumWordsSpoken() {
		return numWordsSpoken;
	}
	public void setNumWordsSpoken(int numWordsSpoken) {
		this.numWordsSpoken = numWordsSpoken;
	}
	
	public ArrayList<Word> getWords() {
		return wordList;
	}
	
	public void addWords(Word words) {
		wordList.add(words);
	}
	
	public ArrayList<Sentence> getSentences() {
		return sentenceList;
	}
	
	public void addSentence(Sentence sentences) {
		sentenceList.add(sentences);
	}
	
}
