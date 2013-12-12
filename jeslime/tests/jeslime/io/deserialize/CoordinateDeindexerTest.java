package jeslime.io.deserialize;

import io.deserialize.CoordinateDeindexer;
import jeslime.EslimeTestCase;
import junit.framework.TestCase;
import structural.identifiers.Coordinate;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dbborens on 12/10/13.
 */
public class CoordinateDeindexerTest extends EslimeTestCase {

    private ExposedDeindexer deindexer;

    @Override
    public void setUp() {
        deindexer = new ExposedDeindexer(fixturePath);
    }

    public void testIndex() {
        // This coordinate has index 1 -- see test fixture
        Coordinate input = new Coordinate(0, 1, 1);
        assertEquals(1, (int) deindexer.getIndex(input));
    }

    public void testDeindex() {
        Coordinate expected = new Coordinate(0, 1, 1);
        Coordinate actual = deindexer.getCoordinate(1);
        assertEquals(expected, actual);
    }

    public void testParseCoordinate()  {
        String input = "(3, 5, 9 | 0)";
        Coordinate expected = new Coordinate(3, 5, 9, 0);
        Coordinate actual = deindexer.parseCoordinate(input);
        assertEquals(expected, actual);
    }

    private class ExposedDeindexer extends CoordinateDeindexer {

        public HashMap<Integer, Coordinate> indexToCoord;

        public ExposedDeindexer(String path) {
            super(path);
            indexToCoord = super.indexToCoord;
        }

        public void deindex() throws IOException {
            super.deindex();
        }

        public Coordinate parseCoordinate(String token) {
            return super.parseCoordinate(token);
        }
    }
}
