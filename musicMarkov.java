//-------------------------------------------------------------------------
// musicMarkov.java
//
// Takes input of parsed midi input and arranges into markov chains that represent
// a probabilistic copy of the music of the given input.
//
// Alexander Lill and Tyler Batistic
// HackACM 2019
// 4/13/19
//-------------------------------------------------------------------------
import java.util.*;

class musicMarkov {
	// Fields
	double[][] noteMark;
	double[][] lengthMark;
	Map<Integer, String> translateNote; //Maps numbers to notes
    Map<Integer, String> translateRhythm;

	musicMarkov(Object A) {
		noteMark = new double[13][13];
		lengthMark = new double[6][6];
		translateNote = new HashMap<Integer, String>();
		translateRhythm = new HashMap<Integer, String>();

		translateNote.put(0, "C");
		translateNote.put(1, "C");
		translateNote.put(2, "C#");
		translateNote.put(3, "D");
		translateNote.put(4, "D#");
		translateNote.put(5, "E");
		translateNote.put(6, "F");
		translateNote.put(7, "F#");
		translateNote.put(8, "G");
		translateNote.put(9, "G#");
		translateNote.put(10, "A");
		translateNote.put(11, "A#");
		translateNote.put(12, "B");

		translateRhythm.put(0, "half");
		translateRhythm.put(1, "quarter");
		translateRhythm.put(2, "8th");
		translateRhythm.put(3, "16th");
		translateRhythm.put(4, "triplet quarter");
		translateRhythm.put(5, "triplet 8th");
	}

	// Translates midi note number to text
	String translateNote(int num) {
		return translateNote.get(num % 12);
	}
	
	// Translates rhythm number to text
	String translateRhythm(int num) {
		return translateRhythm.get(num);
	}

	// Records transition from note1 to note2 in noteMark
	void putNote(int note1, int note2) {
		noteMark[note1][note2] += 1;
	}

	// Records transition from rhythm1 to rhythm2 in lengthMark
	void putRhythm(int rhythm1, int rhythm2) {
		lengthMark[rhythm1][rhythm2] += 1;
	}

	// Normalizes each column based on weight 
	void calcAverage(double[][] markov) {
		int temp = 0;

		for(int i = 0; i < noteMark.length; i++) {
			for(int j = 0; j < noteMark[].length; j++) { // Calculates column weight
				temp += noteMark[i][j];
			}

			for(int j = 0; j < noteMark[].length; j++) { // Divides each column by total weight
				noteMark[i][j] /= temp;
			}
		}
	}




}
