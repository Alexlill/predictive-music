//---------------------------------------------------------------------------------------------
// MusicWriter.java
// 
// Takes input from file produced by MusicMarkov.java and algorithmically generates 
// midi files based on the calculated probabilities.
//
//---------------------------------------------------------------------------------------------
//
import javax.sound.midi.*;
import java.util.*;
import java.io.*;

class MusicWriter {
	// Fields
	int[][] noteProbMark;
	int[][] lengthProbMark;
	int[] noteList;
	int[] lengthList;
	Random rand;
    
	MusicWriter() throws FileNotFoundException, InvalidMidiDataException, IOException{
		noteProbMark = new int[13][13];
		lengthProbMark = new int[7][7];
		noteList = new int[500];
		lengthList = new int[500];
		rand = new Random();

		for(int i = 0; i < 500; i++) {
			noteList[i] = 0;
			lengthList[i] = 0;
		}

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
		while(i < (this.noteProbMark[currentNote].length) ) {
			if(this.noteProbMark[i][currentNote] > random) {
				return i;
			}
			i++;
		}
		return 0;
	}

	int pickRhythm(int currentRhythm) {
		int random = rand.nextInt(1000000000);

		int i = 0;
		while(i < this.lengthProbMark[currentRhythm].length) {
			if(this.lengthProbMark[i][currentRhythm] > random) {
				return i;
			}
			i++;
		}
		return 1;
	}

	// Writes a song based on current calculated probabilities.
	// PRE: noteProbMark and lengthProbMark already converted with convertProbabilities.
    void writeSong(int[] noteList, int[] lengthList) throws FileNotFoundException, InvalidMidiDataException, IOException {
	    int resolution = 360;
	    Sequence sequence = new Sequence(0, resolution,1);
	    Track track = sequence.getTracks()[0];
	    long tick = 0; //This will be used to find the cumulative ticks over the time of the song
	    int note;
	    int i = 0;
	    int j = 0;
	    MidiMessage messageStart;
	    MidiMessage messageEnd;
	    MidiEvent noteStart;
	    MidiEvent noteEnd;
	    while (i < 250){
		if (noteList[i] == 12){
		    tick +=  lengthFinder(lengthList[i], resolution);
		}
		else{
		    if(i > 0){
			note = octaveHelper(noteList[i-1], noteList[i]) + 69;
			
		    }
		    else{
			note = noteList[i]+69;
		    }
		    if(lengthList[i] == 5)
		    {
				messageStart = new ShortMessage(-112,note,80);
				noteStart = new MidiEvent(messageStart, tick);
				tick += lengthFinder(lengthList[i], resolution);
				messageEnd = new ShortMessage(-112,note,0);
				noteEnd = new MidiEvent(messageEnd, tick - 20);
				track.add(noteStart);
				track.add(noteEnd);

				note = noteList[i+1]+69;
				messageStart = new ShortMessage(-112,note,80);
				noteStart = new MidiEvent(messageStart, tick);
				tick += lengthFinder(lengthList[i], resolution);
				messageEnd = new ShortMessage(-112,note,0);
				noteEnd = new MidiEvent(messageEnd, tick - 20);
				track.add(noteStart);
				track.add(noteEnd);

				note = noteList[i+2]+69;
		    }
		    if(lengthList[i] == 3) {
				messageStart = new ShortMessage(-112,note,80);
				noteStart = new MidiEvent(messageStart, tick);
				tick += lengthFinder(lengthList[i], resolution);
				messageEnd = new ShortMessage(-112,note,0);
				noteEnd = new MidiEvent(messageEnd, tick - 20);
				track.add(noteStart);
				track.add(noteEnd);

				note = noteList[i+1]+69;

				messageStart = new ShortMessage(-112,note,80);
				noteStart = new MidiEvent(messageStart, tick);
				tick += lengthFinder(lengthList[i], resolution);
				messageEnd = new ShortMessage(-112,note,0);
				noteEnd = new MidiEvent(messageEnd, tick - 20);
				track.add(noteStart);
				track.add(noteEnd);


				note = noteList[i+2]+69;
				messageStart = new ShortMessage(-112,note,80);
				noteStart = new MidiEvent(messageStart, tick);
				tick += lengthFinder(lengthList[i], resolution);
				messageEnd = new ShortMessage(-112,note,0);
				noteEnd = new MidiEvent(messageEnd, tick - 20);
				track.add(noteStart);
				track.add(noteEnd);

				note = noteList[i+3]+69;

			}
		    messageStart = new ShortMessage(-112,note,80);
		    noteStart = new MidiEvent(messageStart, tick);
		    tick += lengthFinder(lengthList[i], resolution);
		    messageEnd = new ShortMessage(-112,note,0);
		    noteEnd = new MidiEvent(messageEnd, tick - 20);
		    track.add(noteStart);
		    track.add(noteEnd);
		}
		i++;
	    }
	    FileOutputStream file = new FileOutputStream("out.mid");
	    MidiSystem.write(sequence, 0, file);
    }

    int lengthFinder(int noteLength, int resolution){
	switch (noteLength){
	    case 0:
		return resolution*2;
	    case 1:
		return resolution;
	    case 2:
		return resolution/2;
	    case 3:
		return resolution/4;
	    case 4:
		return (resolution*2)/3;
	    case 5:
		return resolution/3;
	    case 6:
		return resolution/2;
	    case 7:
		return resolution + resolution/2;
	}
	return resolution/2;
    }
	// Creates an array of notes and rhythms to be used in song creation.
	// PRE: noteProbMark and lengthProbMark already converted with convertProbabilities().
	void initNotes() {
		noteList[0] = lengthList[0] = rand.nextInt(6);
		for(int i = 1; i < noteList.length; i++) {
			noteList[i] = pickNote(noteList[i-1]);
			lengthList[i] = pickRhythm(lengthList[i-1]);
		}
	}


	public static void main(String[] args) throws FileNotFoundException, InvalidMidiDataException, IOException{
		ReadMidi reader = new ReadMidi("Donna_Lee.mid");
		MusicMarkov test = new MusicMarkov();
		MusicWriter writer = new MusicWriter();
		Random a = new Random();
		List<String> names = new ArrayList<String>();

		names.add("Another_Hairdo.mid");
		names.add("Anthropology.mid");
		names.add("Au_Private_1.mid");
		names.add("Au_Private_2.mid");
		names.add("Back_Home_Blues.mid");
		names.add("Barbados.mid");
		names.add("Billie's_Bounce.mid");
		names.add("Blues_For_Alice.mid");
		names.add("Buzzy.mid");
		names.add("Card_Board.mid");
		names.add("Celerity.mid");
		names.add("Cheryl.mid");
		names.add("Chi_Chi.mid");
		names.add("Dewey_Square.mid");
		names.add("Kim_1.mid");
		names.add("Kim_2.mid");
		names.add("My_Little_Suede_Shoes.mid");
		names.add("Ornithology.mid");
		names.add("Passport.mid");
		names.add("Bird_Gets_The_Worm.mid");
		names.add("Bloomdido.mid");
		names.add("Chasing_The_Bird.mid");
		names.add("Confirmation.mid");
		names.add("Yardbird_Suite.mid");


		test.putNote(1, 2);
		test.putRhythm(1, 2);

		for(int j = 0; j < names.size(); j++) {
			reader = new ReadMidi(names.get(j));		
			for(int i = 1; i < reader.noteArray.length - 1; i++) {
				test.putNote(reader.noteArray[i-1], reader.noteArray[i]);
				test.putRhythm(reader.lengthArray[i-1], reader.lengthArray[i]);
			}
		}

		for(int i = 0; i < test.noteMark.length; i++) {
			for(int j = 0; j < test.noteMark.length; j++) {
				System.out.print(test.noteMark[i][j] + " ");
			}
		}

		

		test.calcAverage(test.noteMark);
		test.calcAverage(test.lengthMark);

		writer.noteProbMark = writer.convertProbabilities(test.noteMark);
		writer.lengthProbMark = writer.convertProbabilities(test.noteMark);
		writer.initNotes();
		writer.writeSong(writer.noteList, writer.lengthList);
	}
}
	

