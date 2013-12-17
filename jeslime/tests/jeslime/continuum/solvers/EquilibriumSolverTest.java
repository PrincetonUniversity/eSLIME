package jeslime.continuum.solvers;

import continuum.operations.*;
import continuum.solvers.EquilibriumSolver;

import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.shape.*;
import geometry.lattice.*;
import geometry.boundaries.Arena;

import io.serialize.ContinuumStateWriter;
import jeslime.EslimeTestCase;
import jeslime.mock.MockGeneralParameters;
import jeslime.mock.MockSoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import structural.EpsilonUtil;
import structural.Flags;
import structural.MatrixUtils;
import structural.identifiers.Coordinate;
import structural.postprocess.SolutionViewer;
/**
 * Tests that an equilibrium solver produces the expected
 * answers to within machine epsilon.
 *
 */
public abstract class EquilibriumSolverTest extends EslimeTestCase {

    // The solver to use in the tests.
    protected EquilibriumSolver solver;

    // The source vector to use in the tests. Generally, this should
    // be a single source in the center of the lattice.
    protected DenseVector source;

    protected Geometry geometry;

    /**
     * setUp should instantiate a solver in the "solver"
     * field, which will then be used by each of the tests.
     *
     */
    public abstract void constructSolver();

    /* Identity matrix checkers */

    /**
     * Asserts that the solver performs the identity operation
     * correctly.
     */
    public void testIdentityAbsolute() {
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 4, 4);

        // Finish constructing geometry

        Boundary boundary = new Arena(shape, lattice);
        geometry = new Geometry(lattice, shape, boundary);

        // Construct solver apparatus
        Operator operator = new Scaling(geometry, false, 1.0);
        operator.init();

        constructSolver();
        solver.respecify(operator);

        // Set each coordinate to a different value.
        source = new DenseVector(geometry.getCanonicalSites().length);
        double f = 1.0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Coordinate c = new Coordinate(x, y, 0);
                Integer i = geometry.coordToIndex(c);
                source.set(i, f);
                f += 1.0;
            }
        }

        // Apply the identity operation.
        SolutionViewer result = solver.solve(source);
        DenseVector solution = result.getSolution();

        // Verify that every site has its original value.
        double expected = 1.0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Coordinate c = new Coordinate(x, y, 0);
                Integer i = geometry.coordToIndex(c);
                double actual = source.get(i);
                assertEquals(expected, actual, EpsilonUtil.epsilon());
                source.set(i, f);
                expected += 1.0;
            }
        }
    }

    /**
     * Make assertions from predecessor modeling projects
     * that use triangular lattices.
     *
     */
    public void testLegacyTri() {
        double p_0_1 =  0.0096534397370393;
        double p_0_2 =  0.0061330674939363;
        double p_1_2 =  0.0068005591091883;
        double p_0_15 = 0.00019134725582351;
        double p_0_100 = 0D;

        // See testTriangularLattice below.
        buildRectangularExample(1e-5, 6.25e-7);
        SolutionViewer result = solver.solve(source);

        // Test that solver agrees with test expectations from prior projects.
        testCoordinate(0, 1, p_0_1, result);
        testCoordinate(0, 2, p_0_2, result);
        testCoordinate(1, 2, p_1_2, result);
        testCoordinate(0, 15, p_0_15, result);
        testCoordinate(0, 100, p_0_100, result);
    }

    /**
     * Make assertions from predecessor modeling projects
     * that use rectangular lattices.
     */
    public void testLegacyRect() {
        // See testRectangularLattice below.
        buildRectangularExample(1e-5, 6.25e-7);
        SolutionViewer result = solver.solve(source);

        // Test that solver agrees with test expectations from prior projects.
        double p_3_2 =  0.004764009307677;
        double p_1_0 =  0.015681143111794;
        double p_0_0 =  0.030824425246585;
        double p_15_0 = 1.423997343559253E-4;
        double p_50_0 = 0D;

        DenseVector solution = result.getSolution();

        System.out.println(MatrixUtils.matrixForm(MatrixUtils.vectorToMatrix(solution, 64)));

        testCoordinate(3, 2, p_3_2, result);
        testCoordinate(1, 0, p_1_0, result);
        testCoordinate(0, 0, p_0_0, result);
        testCoordinate(15, 0, p_15_0, result);
        testCoordinate(50, 0, p_50_0, result);
    }

    private void testCoordinate(int dx, int dy, double expected, SolutionViewer result) {
        Coordinate c = new Coordinate(dx, dy, Flags.VECTOR);
        double actual = result.get(c);
        assertEquals(expected, actual, EpsilonUtil.epsilon());
    }

    @SuppressWarnings("UnusedDeclaration")
    private void testCoordinate(int dx, int dy, int dz, double expected, SolutionViewer result) {
        Coordinate c = new Coordinate(dx, dy, dz, Flags.VECTOR);
        double actual = result.get(c);
        assertEquals(expected, actual, EpsilonUtil.epsilon());
    }

    /**
     * Test solving on rectangular lattice from a fixture.
     */
    public void testRectangularLattice() {
        /*
          Parameters based on TestBaseSoluteManager from project growth_sq:
          r = 1e-5
          production = 1 (time^-1)
          decay = 1 (time^-1)
          diffusion = 16 (area / time)

          Parameters are then scaled to dt = diffusion / r.
         */

        buildRectangularExample(1e-5, 6.25e-7);
        generateTestOutput(100);
        fail("Compare me to a fixture.");
    }

    /**
     * Generate test output for fixture-based tests.
     *
     * @param id The layer ID for the test. Should be distinct
     *           for each test condition.
     */
    private void generateTestOutput(Integer id) {
        // Get test result
        SolutionViewer result = solver.solve(source);

        // Construct mocks for writer
        MockSoluteLayer layer = new MockSoluteLayer(id);
        layer.setState(result.getSolution());

        MockGeneralParameters p = new MockGeneralParameters();
        p.setInstancePath(fixturePath);

        // Construct writer
        ContinuumStateWriter writer = new ContinuumStateWriter(geometry, p);
        writer.init(layer);
        writer.step(0, 0);
        writer.dispatchHalt(null);
    }

    /**
     * Test solving on triangular lattice from a fixture.
     */
    public void testTriangularLattice() {
        /*
          Parameters based on TestBaseSoluteManager from project growth_hex:
          r = 1e-5
          production = 1 (time^-1)
          decay = 1 (time^-1)
          diffusion = 25 (area / time)

          Parameters are then scaled to dt = diffusion / r.
         */

        buildTriangularExample(1e-5, 4e-7);
        generateTestOutput(200);
        fail("Compare me to a fixture.");
    }

    /**
     * Test solving on cubic (3D rectilinear) lattice from a fixture.
     */
    public void testCubicLattice() {
        // Using same parameters as those from testTriangularLattice (why not?)

        buildCubicExample(1e-5, 4e-7);
        generateTestOutput(300);
        fail("Compare me to a fixture.");

    }


    private void buildRectangularExample(double diffusion, double decay) {
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 64, 64);

        finishSetup(lattice, shape, diffusion, decay);

    }

    private void finishSetup(Lattice lattice, Shape shape, double diffusion, double decay) {
        // Finish constructing geometry

        Boundary boundary = new Arena(shape, lattice);
        geometry = new Geometry(lattice, shape, boundary);

        // Construct solver apparatus
        Operator operator = makeCompoundOperator(geometry, diffusion, decay);
        constructSolver();
        solver.respecify(operator);

        Coordinate center = shape.getCenter();
        int centerIndex = geometry.coordToIndex(center);

        source = new DenseVector(geometry.getCanonicalSites().length);
        source.set(centerIndex, 1.0);
    }


    private void buildTriangularExample(double diffusion, double decay) {
        Lattice lattice = new TriangularLattice();
        Shape shape = new Hexagon(lattice, 48);

        finishSetup(lattice, shape, diffusion, decay);

    }

    private void buildCubicExample(double diffusion, double decay) {
        Lattice lattice = new CubicLattice();
        Shape shape = new Cuboid(lattice, 32, 32, 32);

        finishSetup(lattice, shape, diffusion, decay);
    }

    protected Operator makeCompoundOperator(Geometry geometry, double diffusion, double decay) {
        double negativeDiffusion = -1.0 * diffusion;

        Operator[] children = new Operator[] {
                new Scaling(geometry, false, decay),
                new Scaling(geometry, false, 1.0),
                new Diffusion(geometry, false, negativeDiffusion)
        };

        for (Operator child : children) {
            child.init();
        }

//        for (int i = 0; i < children.length; i++) {
//            System.out.println("\n\nCHILD " + i +"\n");
//            System.out.println(children[i]);
//        }

        CompoundOperator operator = new CompoundOperator(geometry, true, children);
        operator.init();
//        System.out.println("\n\n******\nPARENT\n******\n");
//        System.out.println(operator);
        return operator;
    }


}
