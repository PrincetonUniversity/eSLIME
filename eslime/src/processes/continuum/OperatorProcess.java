/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import control.halt.HaltCondition;
import no.uib.cipr.matrix.DenseMatrix;
import processes.BaseProcessArguments;
import processes.StepState;

import java.util.function.Consumer;

/**
 * Supplies an operator matrix to a consumer (typically a continuum
 * scheduler) when fired.
 *
 * Created by dbborens on 1/22/15.
 */
public class OperatorProcess extends ContinuumProcess {

    private DenseMatrix operator;
    private Consumer<DenseMatrix> target;

    public OperatorProcess(BaseProcessArguments arguments, DenseMatrix operator, Consumer<DenseMatrix> target) {
        super(arguments);
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        target.accept(operator);
    }

    @Override
    public void init() {}
}
