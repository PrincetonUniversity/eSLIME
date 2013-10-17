package control;

import java.io.File;
import java.util.Iterator;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import org.dom4j.*;
import org.dom4j.io.*;

import structural.identifiers.Coordinate;

public class Sandbox {

	public static void main(String[] args)
	{
	    try {
	        Synthesizer synthesizer = MidiSystem.getSynthesizer();
	        synthesizer.open();

	        MidiChannel[] channels = synthesizer.getChannels();

	        channels[0].noteOn(600, 10);
	        Thread.sleep(20000);
	        channels[0].noteOff(600);

	        synthesizer.close();
	    } catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}

}
