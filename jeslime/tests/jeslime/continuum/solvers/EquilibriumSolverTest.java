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
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import structural.EpsilonUtil;
import structural.Flags;
import structural.MatrixUtils;
import structural.identifiers.Coordinate;
import structural.postprocess.SolutionViewer;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests that an equilibrium solver produces the expected
 * answers to within machine epsilon.
 *
 */
public abstract class EquilibriumSolverTest extends EslimeTestCase {

    protected static final int RECTANGULAR_DIM = 64;
    protected static final int TRIANGULAR_DIM = 48;
    protected static final int CUBIC_DIM = 32;

    // The solver to use in the tests.
    protected EquilibriumSolver solver;

    // The source vector to use in the tests. Generally, this should
    // be a single source in the center of the lattice.
    protected DenseVector source;

    protected Geometry geometry;
    protected Operator operator;

    /**
     * Should instantiate a solver in the "solver"
     * field, which will then be used by each of the tests.
     *
     * @param operator
     */
    public abstract void constructSolver(Matrix operator);


    /* Step 1: Verify operator correctness. */
    /**
     * Make sure that no diffusion and no decay implies identity.
     */
    public void testDegenerateCaseIsIdentity() {
        buildRectangularExample(0D, 0D, 0D);
        for (int i = 0; i < RECTANGULAR_DIM; i++) {
            for (int j = 0; j < RECTANGULAR_DIM; j++) {
                if (i == j) {
                    assertEquals(1.0, operator.get(i, j), EpsilonUtil.epsilon());
                } else {
                    assertEquals(0.0, operator.get(i, j), EpsilonUtil.epsilon());
                }
            }
        }
    }

    /**
     * Make sure that complete decay without diffusion is the zero matrix.
     */
    public void testCompleteDecayIsZero() {
        buildRectangularExample(0D, 1D, 0D);
        for (int i = 0; i < RECTANGULAR_DIM; i++) {
            for (int j = 0; j < RECTANGULAR_DIM; j++) {
               assertEquals(0.0, operator.get(i, j), EpsilonUtil.epsilon());
            }
        }
    }

    /**
     * Make sure that complete diffusion without decay in a rectangular lattice
     * gives 0.25 at each neighbor and 0 along the main diagonal.
     */
    public void testCompleteDiffusionMatrix() {
        buildRectangularExample(0.25D, 0D, 0D);

        // Are these backward?
        for (int i = 0; i < RECTANGULAR_DIM; i++) {
            Coordinate site = geometry.getCanonicalSites()[i];
            Coordinate[] neighbors = geometry.getNeighbors(site, Geometry.EXCLUDE_BOUNDARIES);
            for (Coordinate neighbor : neighbors) {
                int j = geometry.coordToIndex(neighbor);
                assertEquals(0.25D, operator.get(i, j), EpsilonUtil.epsilon());
            }
        }
    }

    /**
     * Make sure that respecifying an equilibrium solver to A results
     * in the matrix I - A when A != I.
     */
    public void testRespecifyNonIdentity() {

        // Build a matrix consisting of all 1s.
        DenseMatrix m = new DenseMatrix(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                m.set(i, j, 1.0);
            }
        }

        // Initialize solver to it.
        geometry = null;
        constructSolver(m);

        // Make sure that you end up with 0 along the diagonal and -1 everywhere else.
        Matrix actual = solver.getOperator();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == j) {
                    assertEquals(0.0D, actual.get(i, j), EpsilonUtil.epsilon());
                } else {
                    assertEquals(-1.0D, actual.get(i, j), EpsilonUtil.epsilon());
                }
            }
        }
    }

    /**
     * Make sure that respecifying an equilibrium sovler to I results in I.
     */
    public void testRespecifyIdentity() {
        // Build an identity matrix.
        Matrix identity = MatrixUtils.I(10);

        // Initialize the solver to it.
        geometry = null;
        constructSolver(identity);

        // Make sure that you end up with an identity matrix.
        Matrix actual = solver.getOperator();
        assertTrue(MatrixUtils.equal(identity, actual));
    }

    /**
     * Test to make sure that we're building the right matrix. (A test of
     * a test.)
     *
     * TODO: This test does not check whether all entries that should be zero actually are.
     */
    public void testCorrectMatrix() {
        double epsilon = EpsilonUtil.epsilon();

        double diffusion = 1e-5;
        double decay = 6.25e-7;

        // We don't actually use this here, but...
        double production = 6.25e-7;

        //double diagonal = 1.0 - (4 * diffusion) - decay;

        buildRectangularExample(diffusion, decay, production);
        Coordinate[] sites = geometry.getCanonicalSites();

        double diagonal = 1.0 - (4.0 * diffusion) - decay;

        for (int i = 0; i < operator.numRows(); i++) {
            Coordinate coord =sites[i];

            // Make sure the diagonal has the expected values.
            assertEquals(diagonal, operator.get(i, i), epsilon);

            Set<Coordinate> nonZero = new HashSet<Coordinate>(5);
            nonZero.add(coord);

            // Make sure the neighbors have the diffusion constant as their matrix entry
            for (Coordinate neighbor : geometry.getNeighbors(coord, Geometry.EXCLUDE_BOUNDARIES)) {
                int j = geometry.coordToIndex(neighbor);
                assertEquals(diffusion, operator.get(i, j), epsilon);
                nonZero.add(neighbor);
            }

            // Make sure everything that should be zero actually is
            for (int j = 0; j < operator.numColumns(); j++) {
                Coordinate query = sites[j];

                if (!nonZero.contains(query)) {
                    assertEquals(0D, operator.get(i, j), epsilon);
                }
            }
        }
    }

    /* Step 2: Verify trivial operation solutions. */

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

        constructSolver(operator);

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
                double actual = solution.get(i);
                assertEquals(expected, actual, EpsilonUtil.epsilon());
                source.set(i, f);
                expected += 1.0;
            }
        }
    }

    /* Step 3: Verify legacy operation correctness. */

    /**
     * Make assertions from predecessor modeling projects
     * that use rectangular lattices.
     *
     * See testRectangularLattice below for parameters.
     */
    public void testLegacyRect() {
        // Build test infrastructure.
        buildRectangularExample(1e-5, 6.25e-7, 6.25e-7);

        SolutionViewer result = solver.solve(source);

        // Test that solver agrees with test expectations from prior projects.

        // This is mysterious. The legacy tests had reflective boundary conditions,
        // so you would expect this to fail because the old tests would have lower
        // concentrations. Instead, the old tests have higher concentrations. Why?
        double p_3_2 =  0.004764009307677;
        double p_1_0 =  0.015681143111794;
        double p_0_0 =  0.030824425246585;
        double p_15_0 = 1.423997343559253E-4;
        double p_50_0 = 0D;

        DenseVector solution = result.getSolution();

        // Legacy tests used fixed 10^-14 maximum tolerance.
        testCoordinate(3, 2, p_3_2, result, 1e-14);
        testCoordinate(1, 0, p_1_0, result, 1e-14);
        testCoordinate(0, 0, p_0_0, result, 1e-14);
        testCoordinate(15, 0, p_15_0, result, 1e-14);
        testCoordinate(50, 0, p_50_0, result, 1e-14);
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
        buildRectangularExample(1e-5, 4e-7, 4e-7);
        SolutionViewer result = solver.solve(source);

        // Test that solver agrees with test expectations from prior projects.
        // Legacy tests used fixed 10^-14 maximum tolerance.
        testCoordinate(0, 1, p_0_1, result, 1e-14);
        testCoordinate(0, 2, p_0_2, result, 1e-14);
        testCoordinate(1, 2, p_1_2, result, 1e-14);
        testCoordinate(0, 15, p_0_15, result, 1e-14);
        testCoordinate(0, 100, p_0_100, result, 1e-14);
    }

    /* Step 4: Verify fixture operation correctness. */

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

        buildRectangularExample(1e-5, 6.25e-7, 6.25e-7);
        generateTestOutput(100);
        fail("Compare me to a fixture.");
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

        buildTriangularExample(1e-5, 4e-7, 4e-7);
        generateTestOutput(200);
        fail("Compare me to a fixture.");
    }

    /**
     * Test solving on cubic (3D rectilinear) lattice from a fixture.
     */
    public void testCubicLattice() {
        // Using same parameters as those from testTriangularLattice (why not?)

        buildCubicExample(1e-5, 4e-7, 4e-7);
        generateTestOutput(300);
        fail("Compare me to a fixture.");

    }

    /* Helper methods. */

    private void testCoordinate(int dx, int dy, double expected, SolutionViewer result, double tolerance) {
        Coordinate c = new Coordinate(dx, dy, Flags.VECTOR);
        double actual = result.get(c);
        assertEquals(expected, actual, tolerance);
    }

    @SuppressWarnings("UnusedDeclaration")
    private void testCoordinate(int dx, int dy, int dz, double expected, SolutionViewer result) {
        Coordinate c = new Coordinate(dx, dy, dz, Flags.VECTOR);
        double actual = result.get(c);
        assertEquals(expected, actual, EpsilonUtil.epsilon());
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


    private void buildRectangularExample(double diffusion, double decay, double production) {
        Lattice lattice = new RectangularLattice();

        int l = RECTANGULAR_DIM;
        Shape shape = new Rectangle(lattice, l, l);

        finishSetup(lattice, shape, diffusion, decay, production);

    }

    private void finishSetup(Lattice lattice, Shape shape, double diffusion, double decay, double production) {
        // Finish constructing geometry

        Boundary boundary = new Arena(shape, lattice);
        geometry = new Geometry(lattice, shape, boundary);

        // Construct solver apparatus
        operator = makeCompoundOperator(geometry, diffusion, decay);
        constructSolver(operator);

        Coordinate center = shape.getCenter();
        int centerIndex = geometry.coordToIndex(center);

        source = new DenseVector(geometry.getCanonicalSites().length);
        source.set(centerIndex, production);
    }


    private void buildTriangularExample(double diffusion, double decay, double production) {
        Lattice lattice = new TriangularLattice();

        int l = TRIANGULAR_DIM;
        Shape shape = new Hexagon(lattice, l);

        finishSetup(lattice, shape, diffusion, decay, production);

    }

    private void buildCubicExample(double diffusion, double decay, double production) {
        Lattice lattice = new CubicLattice();

        int l = CUBIC_DIM;
        Shape shape = new Cuboid(lattice, l, l, l);

        finishSetup(lattice, shape, diffusion, decay, production);
    }

    protected Operator makeCompoundOperator(Geometry geometry, double diffusion, double decay) {
        double negDecay = decay * -1.0D;
        Operator[] children = new Operator[] {
                new Scaling(geometry, false, negDecay),
                new Diffusion(geometry, false, diffusion)
        };

        for (Operator child : children) {
            child.init();
        }

        /*for (int i = 0; i < children.length; i++) {
            System.out.println("\n\nCHILD " + i +"\n");
            System.out.println(children[i]);
        }

        System.exit(0);*/
        CompoundOperator operator = new CompoundOperator(geometry, true, children);
        operator.init();
//        System.out.println("\n\n******\nPARENT\n******\n");
//        System.out.println(operator);
        return operator;
    }

}
