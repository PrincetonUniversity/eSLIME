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
		int a = 1;
		int b = 2;
		
		double c = 3.0 * ((double) a / b);
		
		System.out.println(c);
	}

}
