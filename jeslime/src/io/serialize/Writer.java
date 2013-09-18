package io.serialize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Contains convenience methods for 
 * @author dbborens
 *
 */
public abstract class Writer {
	protected void mkDir(String pathStr, boolean recursive) {
		File path = new File(pathStr);
		if (!path.exists()) {
			try {
				if (recursive)
					path.mkdirs();
				else
					path.mkdir();
			} catch (Exception ex) {
				System.out.println("Could not create directory tree " + pathStr);
				throw new RuntimeException(ex);
			}			
		}
	}
	
	protected BufferedWriter makeBufferedWriter(String filename) {
		BufferedWriter bw;
		try {
			File file = new File(filename);
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		
		return bw;
	}
	
	/**
	 * Convenience method for appending the contents of a string builder
	 * to a buffered writer, converting any IO exceptions to runtime
	 * exceptions.
	 * 
	 * @param bw
	 * @param line
	 */
	protected void hAppend(BufferedWriter bw, StringBuilder line) {
		try {
			bw.append(line.toString());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	protected void hClose(BufferedWriter bw) {
		try {
			bw.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
