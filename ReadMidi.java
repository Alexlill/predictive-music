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
	FileInputStream file = new FileInputStream("Yardbird_Suite.mid");
       	Sequence sequence = MidiSystem.getSequence(file);
	Track[] trackArray = sequence.getTracks();
        Track track = trackArray[0];
	MidiEvent event;
	MidiMessage message;
	for (int i = 0; i < track.size(); i++){
	    event = track.get(i);
	    message = event.getMessage();
	    for (int j = 0; j < message.getLength(); j++){
		System.out.print(Byte.toString(message.getMessage()[j]) +" ");
	    }
	    System.out.print("\n");
	}
    }
    
public void ReadMidi() throws FileNotFoundException, InvalidMidiDataException, IOException {
	FileInputStream file = new FileInputStream("Yardbird_Suite.mid");
       	Sequence sequence = MidiSystem.getSequence(file);
	Track[] trackArray = sequence.getTracks();
	Track track = trackArray[0];
	MidiEvent event = track.get(0);
	MidiMessage message = event.getMessage();
	System.out.println(message.getMessage());
	//FileInputStream file = new FileInputStream("testfile.txt");
	//	Sequence sequence = MidiSystem.getSequence(file);
	//Track[] trackArray = sequence.getTracks();
    }
    
}
