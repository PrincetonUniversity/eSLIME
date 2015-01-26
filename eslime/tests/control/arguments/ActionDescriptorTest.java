/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import agent.action.Action;
import cells.BehaviorCell;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ActionDescriptorTest {

    private Function<BehaviorCell, Action> constructor;
    private ActionDescriptor<Action> query;
    private BehaviorCell cell;

    @Before
    public void init() throws Exception {
        constructor = (Function<BehaviorCell, Action>) mock(Function.class);
        cell = mock(BehaviorCell.class);
        query = new ActionDescriptor<>(constructor);
    }

    @Test
    public void instantiateCallsFunction() throws Exception {
        query.instantiate(cell);
        verify(constructor).apply(cell);
    }
}