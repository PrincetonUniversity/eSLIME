/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;


import org.junit.Test;

import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RemoverIndexTest {

    @Test
    public void add() throws Exception {
        HashSet<Runnable> removers = (HashSet<Runnable>) mock(HashSet.class);
        RemoverIndex query = new RemoverIndex(removers);
        Runnable runnable = mock(Runnable.class);
        query.add(runnable);
        verify(removers).add(runnable);
    }

    @Test
    public void removeFromAllRunsRunners() throws Exception {
        HashSet<Runnable> removers = new HashSet<>();
        Runnable runnable = mock(Runnable.class);
        removers.add(runnable);
        RemoverIndex query = new RemoverIndex(removers);
        query.removeFromAll();
        verify(runnable).run();
    }
}