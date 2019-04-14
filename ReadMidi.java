//-------------------------------------------------------------------------
// ReadMidi.java
//
// Take a midi file and output a parsed file of notes and rests to be read by
// musicMarkov to create a markov chain.
//
// Alexander Lill and Tyler Batistic
// HackACM 2019
// 4/13/19
//-------------------------------------------------------------------------
import java.util.*;
import java.io.*;
import javax.sound.midi.*;

public class ReadMidi{
    public int[] noteArray;
    public int[] lengthArray;
    
    ReadMidi(String filepath)  throws FileNotFoundException, InvalidMidiDataException, IOException {

	FileInputStream file = new FileInputStream(filepath); //Load file into input stream
	Sequence sequence = MidiSystem.getSequence(file);     //Open
	file.close();                                         // Close the FileInputStream
	int resolution = sequence.getResolution();            // The "tempo" in ticks per quarter note	
	
	Track[] trackArray = sequence.getTracks();            // Get array of track from Midi Sequence
	Track track = trackArray[0];                          // Retrieve the solo instrumental track we need
	parseNotes(track, resolution);                        // Turn this track into a set of arrays of notes and timing
    
	}
    
    
    public void parseNotes(Track track, int resolution){
	MidiEvent event;                                  // Declare MidiEvent to be used in loop
	MidiEvent nextEvent;                              // Declare MidiEvent to be used in loop
	MidiMessage message;                              // Declare MidiMessage to be used in loop
	int note;                                         // Declare int for note in loop
	

    int[] noteArrayLong = new int[track.size()-10];       // Declare and initialize array for notes
	int[] lengthArrayLong = new int[track.size()-10];     // Declare and initialize array for note lengths
	int k = 0;                                        // Use for index of arrays
	for (int i = 10; i < track.size()-1; i+=2){
	    event = track.get(i);
	    nextEvent = track.get(i+1);

	    note = (event.getMessage()).getMessage()[1];
	    note = (note-21)%12;                          //Modulo to get the note no matter the octave. 0 = A, 1 - Bb, 2 = A...., 11 = G#
	    noteArrayLong[k] = note;
	    
	    lengthArrayLong[k] = calculateLength(((double) (nextEvent.getTick() - event.getTick())) / (double) resolution);


	    event = track.get(i+1);
	    nextEvent = track.get(i+2);
	    if (((double) (nextEvent.getTick() - event.getTick())) / (double) resolution > 1.4){
		k++;
		noteArrayLong[k] = 12;
		lengthArrayLong[k] = calculateLength(((double) (nextEvent.getTick() - event.getTick())) / (double) resolution);	
	    }
	    k++;
	}
	noteArray = new int[k];
	lengthArray = new int[k];
	for(int i = 0; i < k; i++){
	    lengthArray[i] = lengthArrayLong[i];
	    noteArray[i] = noteArrayLong[i];
	}
    }

    // Used to find the length of notes or rests
    public static int calculateLength(double length){
	//length = length / resolution;
	if (length < 0.19){
	    return 6; //Triplet sixteenth note
	}
	else if (length < 0.27){
	    return 3; // Sixteenth note
	}
	else if (length < 0.38){
	    return 5; // Triplet eighth note
	}
	else if (length < 0.54){
	    return 2; // Eighth note
	}
	else if (length < 0.80){
	    return 4; // Triplet Quarter note
	}
	else if (length < 1.20){
	    return 1; // Quarter note
	}
	else if (length < 1.75){
	    return 7; // Dotted half note
	}
	else{
	    return 0; // Half note
	}
    }

    //Main function for testing
    public static void main(String[] args)  throws FileNotFoundException, InvalidMidiDataException, IOException {
	ReadMidi reader = new ReadMidi("Yardbird_Suite.mid");
	System.out.println(Arrays.toString(reader.noteArray));
	System.out.println(Arrays.toString(reader.lengthArray));
    }
}
