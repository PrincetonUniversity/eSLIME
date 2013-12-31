package io.serialize;

import geometry.Geometry;
import io.project.GeometryManager;
import layers.cell.CellLayer;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CoordinateIndexer extends AbstractCellWriter {

    // This file specifies the relationship between vector index and coordinate.
    private final String COORDMAP_FILENAME = "coordmap.txt";

    public CoordinateIndexer(GeneralParameters p, GeometryManager gm) {
        super(p, geometry);
    }

    protected void makeCoordinateMap() {
        try {

            String coordMapFileStr = p.getInstancePath() + '/' + COORDMAP_FILENAME;
            File coordMapFile = new File(coordMapFileStr);
            FileWriter fw = new FileWriter(coordMapFile);
            BufferedWriter bwp = new BufferedWriter(fw);

            for (Coordinate c : geometry.getCanonicalSites()) {
                StringBuilder sb = new StringBuilder();
                sb.append(geometry.coordToIndex(c));
                sb.append("\t");
                sb.append(c.toString());
                sb.append("\n");
                bwp.append(sb.toString());
            }

            bwp.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void init(CellLayer l) {
        // Doesn't do anything
    }

    @Override
    public void step(Coordinate[] highlights, double gillespie, int frame) {
        // Doesn't do anything
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        // Writes coordinate map of model to disk
        makeCoordinateMap();
    }

    @Override
    public void close() {
        // Doesn't do anything
    }
}
