package continuum.solvers;

import continuum.operations.*;

import geometry.Geometry;
import geometry.boundaries.Absorbing;
import geometry.boundaries.Boundary;
import geometry.boundaries.PlaneRingReflecting;
import geometry.shape.*;
import geometry.lattice.*;

import io.serialize.ContinuumStateWriter;
import test.EslimeTestCase;
import structural.MockGeneralParameters;
import layers.solute.MockSoluteLayer;
import junitx.framework.FileAssert;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import structural.EpsilonUtil;
import structural.Flags;
import structural.MatrixUtils;
import structural.identifiers.Coordinate;
import structural.postprocess.SolutionViewer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests that an equilibrium solver produces the expected
 * answers to within machine epsilon.
 *
 */
public abstract class EquilibriumSolverTest extends EslimeTestCase {

    protected static final int RECTANGULAR_DIM = 41;
    protected static final int TRIANGULAR_DIM = 50;
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
        doTestDegenerateCaseIsIdentity("Absorbing");
        doTestDegenerateCaseIsIdentity("PlaneRingReflecting");
    }

    /**
     * Make sure that complete decay without diffusion is the zero matrix.
     */
    public void testCompleteDecayIsZero() {
        buildRectangularExample("absorbing", 0D, 1D, 0D);
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
        buildRectangularExample("absorbing", 0.25D, 0D, 0D);

        // Are these backward?
        for (int i = 0; i < RECTANGULAR_DIM; i++) {
            Coordinate site = geometry.getCanonicalSites()[i];
            Coordinate[] neighbors = geometry.getNeighbors(site, Geometry.APPLY_BOUNDARIES);
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

        double diffusion = 1e-4;
        double decay = 6.25e-6;

        // We don't actually use this here, but...
        double production = 6.25e-6;

        //double diagonal = 1.0 - (4 * diffusion) - decay;

        buildRectangularExample("absorbing", diffusion, decay, production);
        Coordinate[] sites = geometry.getCanonicalSites();

        double diagonal = 1.0 - (4.0 * diffusion) - decay;

        for (int i = 0; i < operator.numRows(); i++) {
            Coordinate coord =sites[i];

            // Make sure the diagonal has the expected values.
            assertEquals(diagonal, operator.get(i, i), epsilon);

            Set<Coordinate> nonZero = new HashSet<Coordinate>(5);
            nonZero.add(coord);

            // Make sure the neighbors have the diffusion constant as their matrix entry
            for (Coordinate neighbor : geometry.getNeighbors(coord, Geometry.APPLY_BOUNDARIES)) {
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

        Boundary boundary = new Absorbing(shape, lattice);
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
        buildRectangularExample("absorbing", 1e-4, 6.25e-6, 6.25e-6);

        SolutionViewer result = solver.solve(source);

        // Test that solver agrees with test expectations from prior projects.

        DenseVector solution = result.getSolution();

        // It so happens that these agree exactly, because the matrix structure
        // is the same. As it is, the solver is approximate, so 10^(-12) is
        // a reasonable standard.
        double l_0_0       = 0.0308244252465857;
        double l_0_1       = 0.015681143111794024;
        double l_0_2       = 0.009396096077584433;
        double l_1_2       = 0.008169971789498123;
        double l_0_15      = 1.4239973435592538E-4;
        double l_0_100     = 0.0;

        double p_0_0       = 0.0308244252465857;
        double p_0_1       = 0.015681143111794024;
        double p_0_2       = 0.009396096077584433;
        double p_1_2       = 0.008169971789498123;
        double p_0_15      = 1.4239973435592538E-4;
        double p_0_100     = 0.0;

        // Test that solver agrees with test expectations from prior projects
        // to at least 10^(-12) tolerance.
        testCoordinate(0, 0, l_0_0, result, 1e-12);
        testCoordinate(0, 1, l_0_1, result, 1e-12);
        testCoordinate(0, 2, l_0_2, result, 1e-12);
        testCoordinate(1, 2, l_1_2, result, 1e-12);
        testCoordinate(0, 15, l_0_15, result, 1e-12);
        testCoordinate(0, 100, l_0_100, result, 1e-12);

        // Test that solver agrees with current expectations to machine epsilon.
        testCoordinate(0, 0, p_0_0, result, epsilon);
        testCoordinate(0, 1, p_0_1, result, epsilon);
        testCoordinate(0, 2, p_0_2, result, epsilon);
        testCoordinate(1, 2, p_1_2, result, epsilon);
        testCoordinate(0, 15, p_0_15, result, epsilon);
        testCoordinate(0, 100, p_0_100, result, epsilon);
    }

    /**
     * Make assertions from predecessor modeling projects
     * that use triangular lattices.
     *
     */
    public void testLegacyTri() {
        // l_x_x is legacy value. p_x_x is current value.

        // These differ slightly. The matrices are different, in that neighbors
        // are indexed differently. Other than that, they should be identical.
        // As the error is < 10^(-12), and the solver is approximate, I am letting
        // this stand. However, I would like to revisit this point in the future.
        // It would be desirable to verify that the two matrices may be generated
        // from one another by permuting columns and rows.

        double l_0_0 =      0.019458732712560396;
        double p_0_0 =      0.019458732712482003;

        double l_0_1 =      0.009653359743639577;
        double p_0_1 =      0.00965335974356578;

        double l_0_2 =      0.006132997071546091;
        double p_0_2 =      0.006132997071478953;

        double l_1_2 =      0.006800485237957668;
        double p_1_2 =      0.006800485237888667;


        double l_0_15 =     1.913252233828328E-4;
        double p_0_15 =     1.9132522337364754E-4;

        double p_0_100 =    0D;
        double l_0_100 =    0D;

        // See testTriangularLattice below.
        buildTriangularExample("absorbing", 1e-4, 4e-6, 4e-6);
        SolutionViewer result = solver.solve(source);

        // Test that solver agrees with test expectations from prior projects
        // to at least 10^(-12) tolerance.
        testCoordinate(0, 0, l_0_0, result, 1e-12);
        testCoordinate(0, 1, l_0_1, result, 1e-12);
        testCoordinate(0, 2, l_0_2, result, 1e-12);
        testCoordinate(1, 2, l_1_2, result, 1e-12);
        testCoordinate(0, 15, l_0_15, result, 1e-12);
        testCoordinate(0, 100, l_0_100, result, 1e-12);

        // Test that solver agrees with current expectations to machine epsilon.
        testCoordinate(0, 0, p_0_0, result, epsilon);
        testCoordinate(0, 1, p_0_1, result, epsilon);
        testCoordinate(0, 2, p_0_2, result, epsilon);
        testCoordinate(1, 2, p_1_2, result, epsilon);
        testCoordinate(0, 15, p_0_15, result, epsilon);
        testCoordinate(0, 100, p_0_100, result, epsilon);
    }

    /**
     * The unit tests for the operators verify that the matrices
     * are being built correctly, and the legacy tests verify that
     * the output is consistent with previous results, which were
     * verified to agree with analytical expectations (Borenstein,
     * et al. 2013, PLOS ONE, figure S1 / eq. S5). This test verifies
     * that some reasonable assumptions also hold for a different
     * boundary conditions.
     *
     */
    public void testBoundarySanity() throws Exception {
        // Use a rectangular lattice that reflects in Y and is
        // periodic in X. The source is a single point in the center
        // of the distribution.
        buildRectangularExample("PlaneRingReflecting", 1e-4, 6.25e-6, 6.25e-6);


        SolutionViewer result = solver.solve(source);

        // Top and bottom rows should have same concentration at each position
        for (int x = 0; x < RECTANGULAR_DIM; x++) {
            double bottom = getConcentration(x, 0, result, geometry);
            double top = getConcentration(x, RECTANGULAR_DIM - 1, result, geometry);
            assertEquals(bottom, top, epsilon);
        }

        // Rows closer to the center should have higher concentrations than
        // those further from the center.
        for (int y = 0; y < ((RECTANGULAR_DIM -1) / 2); y++) {
            double outer = getConcentration(0, y, result, geometry);
            double inner = getConcentration(0, y+1, result, geometry);
            assertTrue(outer < inner);
        }
    }

    private double getConcentration(int x, int y, SolutionViewer result, Geometry geometry) {
        Coordinate query = new Coordinate(x, y, 0);
        Coordinate offset = geometry.getDisplacement(geometry.getCenter(), query, Geometry.APPLY_BOUNDARIES);
        double ret = result.get(offset);
        return ret;
    }

    /* Step 4: Verify fixture operation correctness. */

    /**
     * Test solving on rectangular lattice from a fixture.
     */
    public void testRectangularLattice() {
        doTestRectangularLattice("Absorbing");
        doTestRectangularLattice("PlaneRingReflecting");
    }
    private void doTestRectangularLattice(String mode) {
        /*
          Parameters based on TestBaseSoluteManager from project growth_sq:
          r = 1e-4
          production = 1 (time^-1)
          decay = 1 (time^-1)
          diffusion = 16 (area / time)

          Parameters are then scaled to dt = diffusion / r.
         */

        buildRectangularExample(mode, 1e-4, 6.25e-6, 6.25e-6);
        generateTestOutput(100);

        String outputFile = outputPath + "solute100.state.txt";
        String fixtureFile = fixturePath + "solute.rect." + mode.toLowerCase() + ".state.txt";

        File expected = new File(fixtureFile);
        File actual = new File(outputFile);

        FileAssert.assertBinaryEquals(expected, actual);
    }

    /**
     * Test solving on triangular lattice from a fixture.
     */
    public void testTriangularLattice() {
        doTestTriangularLattice("absorbing");
    }

    private void doTestTriangularLattice(String mode) {
        /*
          Parameters based on TestBaseSoluteManager from project growth_hex:
          r = 1e-4
          production = 1 (time^-1)
          decay = 1 (time^-1)
          diffusion = 25 (area / time)

          Parameters are then scaled to dt = diffusion / r.
         */

        buildTriangularExample(mode, 1e-4, 4e-6, 4e-6);
        generateTestOutput(200);

        String outputFile = outputPath + "solute200.state.txt";

        // Both tested modes should have the same output
        String fixtureFile = fixturePath + "solute.tri.state.txt";

        File expected = new File(fixtureFile);
        File actual = new File(outputFile);

        FileAssert.assertBinaryEquals(expected, actual);
    }

    /**
     * Test solving on cubic (3D rectilinear) lattice from a fixture.
     */
    public void testCubicLattice() {
        // For now, just test one BC for 3D case. Add more as this
        // gets more aggressively used.
        doTestCubicLattice("absorbing");
    }

    private void doTestCubicLattice(String mode) {
        // Using same parameters as those from testTriangularLattice (why not?)

        buildCubicExample(mode, 1e-4, 4e-6, 4e-6);
        generateTestOutput(300);

        String outputFile = outputPath + "solute300.state.txt";
        String fixtureFile = fixturePath + "solute.cubic.state.txt";

        File expected = new File(fixtureFile);
        File actual = new File(outputFile);
        FileAssert.assertBinaryEquals(expected, actual);
    }

    /* Helper methods. */

    private void testCoordinate(int dx, int dy, double expected, SolutionViewer result, double tolerance) {
        Coordinate c = new Coordinate(dx, dy, Flags.VECTOR);
        double actual = result.get(c);
        //System.out.println(c + " --> " + actual);
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

        DenseVector solution = result.getSolution();
        // Construct mocks for writer
        MockSoluteLayer layer = new MockSoluteLayer(id);
        layer.setState(solution);
        //System.out.println(solution == null);
        //System.out.println(layer.getState() == null);
        MockGeneralParameters p = new MockGeneralParameters();
        p.setInstancePath(outputPath);
        p.setPath(outputPath);

        // Construct writer
        ContinuumStateWriter writer = new ContinuumStateWriter(geometry, p);
        writer.init(layer);
        writer.step(0, 0);
        writer.dispatchHalt(null);
    }


    private void buildRectangularExample(String mode, double diffusion, double decay, double production) {
        Lattice lattice = new RectangularLattice();

        int l = RECTANGULAR_DIM;
        Shape shape = new Rectangle(lattice, l, l);

        finishSetup(lattice, shape, mode, diffusion, decay, production);

    }

    private void finishSetup(Lattice lattice, Shape shape, String mode, double diffusion, double decay, double production) {
        // Finish constructing geometry

        Boundary boundary = makeBoundary(mode, shape, lattice);
        geometry = new Geometry(lattice, shape, boundary);

        // Construct solver apparatus
        operator = makeCompoundOperator(geometry, diffusion, decay);
        constructSolver(operator);

        Coordinate center = shape.getCenter();
        int centerIndex = geometry.coordToIndex(center);

        source = new DenseVector(geometry.getCanonicalSites().length);
        source.set(centerIndex, production);
    }


    private void buildTriangularExample(String mode, double diffusion, double decay, double production) {
        Lattice lattice = new TriangularLattice();

        int l = TRIANGULAR_DIM;
        Shape shape = new Hexagon(lattice, l);

        finishSetup(lattice, shape, mode, diffusion, decay, production);

    }

    private void buildCubicExample(String mode, double diffusion, double decay, double production) {
        Lattice lattice = new CubicLattice();

        int l = CUBIC_DIM;
        Shape shape = new Cuboid(lattice, l, l, l);

        finishSetup(lattice, shape, mode, diffusion, decay, production);
    }

    protected Operator makeCompoundOperator(Geometry geometry, double diffusion, double decay) {
        double negDecay = decay * -1.0D;
        Operator[] children = new Operator[] {
                new Scaling(geometry, true, negDecay),
                new Diffusion(geometry, true, diffusion)
        };

        //System.out.println("Geometry is null? " + (geometry == null));
        for (Operator child : children) {
            child.init();
        }

        CompoundOperator operator = new CompoundOperator(geometry, true, children);
        operator.init();
        return operator;
    }

    private void doTestDegenerateCaseIsIdentity(String mode) {
        buildRectangularExample(mode, 0D, 0D, 0D);
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

    private Boundary makeBoundary(String mode, Shape shape, Lattice lattice) {
        if (mode.equalsIgnoreCase("Arena")) {
            throw new IllegalStateException("Don't use Arena -- it's not compatible with matrix construction.");
        } else if (mode.equalsIgnoreCase("Absorbing")) {
            return new Absorbing(shape, lattice);
        } else if (mode.equalsIgnoreCase("PlaneRingReflecting")) {
            return new PlaneRingReflecting(shape, lattice);
        } else {
            throw new IllegalArgumentException("Boundary mode not recognized.");
        }
    }


}
