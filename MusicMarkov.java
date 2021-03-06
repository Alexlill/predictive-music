//-------------------------------------------------------------------------
// MusicMarkov.java
//
// Takes input of parsed midi input and arranges into markov chains that represent
// a probabilistic copy of the music of the given input.
//
// Alexander Lill and Tyler Batistic
// HackACM 2019
// 4/13/19
//-------------------------------------------------------------------------
import java.util.*;
import java.io.*;

class MusicMarkov {
	// Fields
	double[][] noteMark;
	double[][] lengthMark;
	Map<Integer, String> translateNote; // Maps numbers to notes
    Map<Integer, String> translateRhythm; // Maps numbers to rhythms

	MusicMarkov() {
		noteMark = new double[13][13];
		lengthMark = new double[8][8];
		translateNote = new HashMap<Integer, String>();
		translateRhythm = new HashMap<Integer, String>();

		
		for(int i = 0; i < lengthMark.length; i++) {
			for(int j = 0; j < lengthMark[1].length; j++) {
				lengthMark[i][j] = 0;
			}

		}


		for(int i = 0; i < noteMark.length; i++) {
			for(int j = 0; j < noteMark[1].length; j++) {
				noteMark[i][j] = 0;
			}

		}

		translateNote.put(0, "C");
		translateNote.put(1, "C#");
		translateNote.put(2, "D");
		translateNote.put(3, "D#");
		translateNote.put(4, "E");
		translateNote.put(5, "F");
		translateNote.put(6, "F#");
		translateNote.put(7, "G");
		translateNote.put(8, "G#");
		translateNote.put(9, "A");
		translateNote.put(10, "A#");
		translateNote.put(11, "B");
		translateNote.put(12, "R");

		translateRhythm.put(0, "half");
		translateRhythm.put(1, "quarter");
		translateRhythm.put(2, "8th");
		translateRhythm.put(3, "16th");
		translateRhythm.put(4, "triplet quarter");
		translateRhythm.put(5, "triplet 8th");
		translateRhythm.put(6, "triplet 16th");
		translateRhythm.put(7, "triplet 8th again");
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

		for(int i = 0; i < markov.length; i++) {
			for(int j = 0; j < markov[0].length; j++) { // Calculates column weight
				temp += markov[i][j];
			}

			for(int j = 0; j < markov[0].length; j++) { // Divides each column by total weight
				markov[i][j] /= temp;
			}

			temp = 0;
		}
	}

	// Exports markov chains to a csv file for transfer and use
	void exportMarkov() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("markovData"));
		
		for(int i = 0; i < noteMark.length; i++) {
			for(int j = 0; j < noteMark[0].length; j++) { // Adds each decimal
				writer.write(noteMark[i][j] + ",");
			}

			writer.write("\n");

		}
		
		writer.write("\n");

		for(int i = 0; i < lengthMark.length; i++) {
			for(int j = 0; j < lengthMark[1].length; j++) {
				writer.write(lengthMark[i][j] + ",");
			}

			writer.write("\n");
		}

		writer.close();

	}

	int octaveHelper(int numPrev, int numNext) {
		if((numNext % 12)  == 0) {
			return 12;
		}
		if((Math.abs(numPrev - numNext) % 12) > 5) {
			if(numNext - 6 < 66) {
				return numNext;
			}
			else {
				return numNext - 6;
			}	
		}
		else {
			if(numNext > 88) {
				return numNext - 12;
			}
		}
		return numNext;
	}

	public static void main(String[] args) throws IOException {
		MusicMarkov test = new MusicMarkov();
		Random a = new Random();

		for(int i = 0; i < 2000; i++) {
			test.putNote(Math.abs(a.nextInt() % 13), Math.abs(a.nextInt() % 13));
		}

		
		for(int i = 0; i < 2000; i++) {
			test.putRhythm(Math.abs(a.nextInt() % 7), Math.abs(a.nextInt() % 7));
		}
		
		test.calcAverage(test.noteMark);
		test.calcAverage(test.lengthMark);

		test.exportMarkov();



	}




}
