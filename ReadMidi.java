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
    
    public static void main(String[] args) throws FileNotFoundException, InvalidMidiDataException, IOException {
	openMidi("Yardbird_Suite.mid");
    }
    
    public static void openMidi(String filepath) throws FileNotFoundException, InvalidMidiDataException, IOException {
	FileInputStream file = new FileInputStream(filepath); //Load file into input stream
	Sequence sequence = MidiSystem.getSequence(file); //Open
	file.close();                                     // Close the FileInputStream
	int resolution = sequence.getResolution();        // The "tempo" in ticks per quarter note	
	
	Track[] trackArray = sequence.getTracks();        // Get array of track from Midi Sequence
	Track track = trackArray[0];                      // Retrieve the solo instrumental track we need
	parseNotes(track, resolution);                                // Turn this track into a set of arrays of notes and timing
    }    
    
    public static void parseNotes(Track track, int resolution){
	MidiEvent event;                                  // Declare MidiEvent to be used in loop
	MidiEvent nextEvent;                              // Declare MidiEvent to be used in loop
	MidiMessage message;                              // Declare MidiMessage to be used in loop
	int note;
	int length;
	int[] noteArray = new int[track.size()-10];       // Declare and initialize array for notes
	int[] lengthArray = new int[track.size()-10];     // Declare and initialize array for note lengths
	int k = 0;                                        // Use for index of arrays
	for (int i = 10; i < track.size()-1; i+=2){
	    event = track.get(i);
	    nextEvent = track.get(i+1);

	    note = (event.getMessage()).getMessage()[1];
	    note = (note-21)%12;                          //Modulo to get the note no matter the octave. 0 = A, 1 - Bb, 2 = A...., 11 = G#
	    noteArray[k] = note;
	    
	    length = (nextEvent.getTick() - event.getTick())/resolution;
	    lengthArray[k] = length;
	    k++;
	}
	System.out.println(Arrays.toString(noteArray));
	System.out.println(Arrays.toString(lengthArray));
    }
}
