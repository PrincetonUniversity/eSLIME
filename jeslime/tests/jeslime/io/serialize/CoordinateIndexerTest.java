package jeslime.io.serialize;

import io.serialize.CoordinateIndexer;
import jeslime.EslimeTestCase;
import jeslime.mock.MockGeneralParameters;
import jeslime.mock.MockGeometry;
import junitx.framework.FileAssert;
import structural.identifiers.Coordinate;
import java.io.File;

/**
 * Created by dbborens on 12/10/13.
 */
public class CoordinateIndexerTest extends EslimeTestCase {

    private MockGeometry geom;
    private MockGeneralParameters params;
    private CoordinateIndexer indexer;

    public void setUp() {
        geom = new MockGeometry();

        Coordinate[] canonicals = new Coordinate[] {
            new Coordinate(0, 0, 0),
            new Coordinate(0, 1, 0),
            new Coordinate(1, 0, 0),
            new Coordinate(1, 1, 0)
        };

        geom.setCanonicalSites(canonicals);
        params = new MockGeneralParameters();
        params.setInstancePath(outputPath);

        indexer = new CoordinateIndexer(params, geom);
    }

    public void testCoordinateIndexer() throws Exception {
        // dispatchHalt instructs the indexer to build the index
        indexer.dispatchHalt(null);

        // Compare output to fixture
        String fixture = fixturePath + "coordmap.txt";
        String output  = outputPath + "coordmap.txt";

        File fixtureFile = new File(fixture);
        File outputFile = new File(output);

        FileAssert.assertEquals(fixtureFile, outputFile);

        // Clean up
        outputFile.delete();
    }

}
