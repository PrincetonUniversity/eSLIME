package io.serialize;

import geometry.Geometry;
import io.project.GeometryManager;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by dbborens on 12/11/13.
 */
public abstract class Writer {
    protected GeometryManager gm;
    protected GeneralParameters p;
    protected boolean closed = true;

    public Writer(GeometryManager gm, GeneralParameters p) {
        this.gm = gm;
        this.p = p;
    }

    /**
     * Signal that the current instance is concluded. Conclude analysis, finish
     * writing to files, and close handles for instance.
     */
    public abstract void dispatchHalt(HaltCondition ex);

    protected void makeFiles() {
        // Create the directory for state files, if needed
        mkDir(p.getPath(), true);

        mkDir(p.getInstancePath(), true);

        System.out.println(p.getInstancePath());
    }

    /**
     * Conclude analysis, finish writing to files, and close for entire project.
     */
    public abstract void close();

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
