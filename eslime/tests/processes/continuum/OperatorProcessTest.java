/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import no.uib.cipr.matrix.DenseMatrix;
import org.junit.Before;
import org.junit.Test;
import processes.BaseProcessArguments;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class OperatorProcessTest {

    private BaseProcessArguments arguments;
    private DenseMatrix operator;
    private Consumer<DenseMatrix> target;
    private OperatorProcess query;

    @Before
    public void init() {
        arguments = mock(BaseProcessArguments.class);
        operator = mock(DenseMatrix.class);
        target = (Consumer<DenseMatrix>) mock(Consumer.class);
        query = new OperatorProcess(arguments, operator, target);
    }


    @Test
    public void fireReportsMatrixToTarget() throws Exception {
        query.fire(null);
        verify(target).accept(operator);
    }

    @Test
    public void initDoesNothing() throws Exception {
        query.init();
        verifyNoMoreInteractions(arguments, operator, target);
    }

}