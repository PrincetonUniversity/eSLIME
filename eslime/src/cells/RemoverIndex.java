/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import java.util.HashSet;

/**
 * Created by dbborens on 12/31/14.
 */
public class RemoverIndex {
    private HashSet<Runnable> removers;

    public RemoverIndex(HashSet<Runnable> removers) {
        this.removers = removers;
    }

    public void add(Runnable remove) {
        removers.add(remove);
    }

    public void removeFromAll() {
        removers.forEach(Runnable::run);
    }

}
