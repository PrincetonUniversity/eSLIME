package layers.cell;

import cells.Cell;
import structural.identifiers.Coordinate;

import java.util.IdentityHashMap;

/**
 * Class for associating cells to coordinates, with
 * cells identified by reference (==) rather than
 * object equality.
 *
 * Created by David B Borenstein on 2/5/14.
 */
public class CellLocationIndex {

    protected IdentityHashMap<Cell, Coordinate> locationMap;

    public CellLocationIndex() {
        locationMap = new IdentityHashMap<>();
    }

    public void place(Cell cell, Coordinate coordinate) {
        if (locationMap.containsKey(cell)) {
            throw new IllegalStateException("Attempting to overwrite existing location key.");
        }

        locationMap.put(cell, coordinate);
    }

    public void move(Cell cell, Coordinate coordinate) {
       if (!locationMap.containsKey(cell))  {
           throw new IllegalStateException("Attempting to move a cell that does not have an indexed spatial location.");
       }

        remove(cell);
        place(cell, coordinate);
    }

    public void remove(Cell cell) {
        if (!locationMap.containsKey(cell))  {
            throw new IllegalStateException("Attempting to remove a cell that does not have an indexed spatial location.");
        }

        locationMap.remove(cell);
    }

    public Coordinate locate(Cell cell) {
        if (!locationMap.containsKey(cell))  {
            throw new IllegalStateException("Attempting to locate a cell that does not have an indexed spatial location.");
        }

        return locationMap.get(cell);

    }

    public boolean isIndexed(Cell cell) {
       return locationMap.containsKey(cell);
    }
}
