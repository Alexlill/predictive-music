//---------------------------------------------------------------------------------------------
// MusicWriter.java
// 
// Takes input from file produced by MusicMarkov.java and algorithmically generates 
// midi files based on the calculated probabilities.
//
//---------------------------------------------------------------------------------------------

import java.util.*;
import java.io.*;
import javax.sound.midi.*;

class MusicWriter {
	// Fields
	int[][] noteProbMark;
	int[][] lengthProbMark;
	Random rand;

	// Constructor
	MusicWriter() {
		noteProbMark = new int[13][13];
		lengthProbMark = new int[8][8];
		rand = new Random();

		for(int i = 0; i < noteProbMark.length; i++) {
			for(int j = 0; j < noteProbMark[0].length; j++) {
				noteProbMark[i][j] = 0;
			}
		}

		for(int i = 0; i < lengthProbMark.length; i++) {
			for(int j = 0; j < lengthProbMark[0].length; j++) {
				lengthProbMark[i][j] = 0;
			}
		}

	}

	// Converts given array from probabilities to integers for easier random calculations.
	int[][] convertProbabilities(double[][] arr) {
		int total = 0;
		int[][] converted = new int[arr.length][arr[0].length];

		for(int j = 0; j < arr[0].length; j++) {
			for(int i = 0; i < arr.length; i++) {
				total += (int) 1000000000 * arr[i][j];
				converted[i][j] = total;
			}

			total = 0;

		}

		return converted;
	}
 
	// Given the current note of the sequence, pick the next based on probability.
	// Pre: noteProbMark and lengthProbMark already converted using convertProbabilities()
	int pickNote(int currentNote) {
		int random = rand.nextInt(1000000000);

		int i = 0;
		while(i < this.noteProbMark[currentNote].length - 1) {
			if(this.noteProbMark[i][currentNote] > random) {
				System.out.println(i + ",");
				return i - 1;
			}
			i++;
		}
			
		return 13;
	}	

	int pickRhythm(int currentRhythm) {
		int random = rand.nextInt(1000000000);

		int i = 0;
		while(i < this.lengthProbMark[currentRhythm].length - 1) {
			if(this.lengthProbMark[i][currentRhythm] > random) {
				System.out.println(i + " ");
				return i - 1;
			}
			i++;
		}
		return 8;
	}



	// Returns a given note in an octave appropriate for sax
	// NOTE: MIGHT NEED TO PUT LOGIc
	int octaveHelper(int note) {
		// Lowest: Db3 = 59
		// Highest: Ab5(?) = 80

		return 0;
	}

	// Writes and outputs a song.
	// PRE: noteProbMark and lengthProbMark already converted using convertProbabilities();
	File writeSong() {
		MidiTrack tempo = new MidiTrack();
		MidiTrack notes = new MidiTrack();
		TimeSignature normal = new TimeSignature();

		normal.setTimeSignature(4, r, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
		return null;	
	}

 

	///////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		MusicWriter writer = new MusicWriter();
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

		writer.noteProbMark = writer.convertProbabilities(test.noteMark);
		writer.lengthProbMark = writer.convertProbabilities(test.lengthMark);


		for(int i = 0; i < writer.noteProbMark.length; i++) {
			for(int j = 0; j < writer.noteProbMark[0].length; j++) {
				System.out.print(writer.noteProbMark[i][j] + ",");
			}
			System.out.print("\n");
		}
	
	System.out.println("\n\n\n");
	
	/*
	for(int i = 0; i < 20; i++) {
		writer.pickNote(i % 13);
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n");
		writer.pickRhythm(i % 8);
	}*/



	}

}
	

