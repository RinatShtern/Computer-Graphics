package primitives;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for the {@link primitives.Vector} class.
 * This class tests various methods related to the Vector class, ensuring their correctness.
 */
public class VectorTests {

    Vector v1 = new Vector(1, 2, 3);
    Vector v2 = new Vector(-2, -4, -6);
    Vector v3 = new Vector(0, 3, -2);

    /**
     * Test method for the constructor of {@link primitives.Vector}.
     * This test ensures that creating a zero vector throws an exception.
     */
    @Test
    void testZero() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        try { // test zero vector
            new Vector(0, 0, 0);
            fail("ERROR: zero vector does not throw an exception");
        } catch (IllegalArgumentException e) {
            out.println("Good: vector 0 not created");
        }
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     * This test checks the calculation of the squared length of vectors.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        if (!isZero(v1.lengthSquared() - 14))
            fail("ERROR: lengthSquared() wrong value");
        if (!isZero(new Vector(0, 3, 4).length() - 5))
            fail("ERROR: length() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     * This test checks the correctness of the cross product calculation between vectors.
     */
    @Test
    public void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v123 = new Vector(0, 0, 1);
        Vector v03M2 = new Vector(1, 0, 0);
        Vector vr = v123.crossProduct(v03M2);

        assertEquals(0, vr.dotProduct(v123));
        assertEquals(0, vr.dotProduct(v03M2));
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     * This test checks the addition of two vectors.
     */
    @Test
    void testAdd() {
        Vector v1 = new Vector(1.0, 2.0, 3.0);
        Vector v2 = new Vector(4.0, 5.0, 6.0);

        Vector result = v1.add(v2);

        assertEquals(new Vector(5.0, 7.0, 9.0), result);
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     * This test checks the dot product calculation between vectors.
     */
    @Test
    void testDotProduct() {
        // test Dot-Product
        if (!isZero(v1.dotProduct(v3)))
            fail("ERROR: dotProduct() for orthogonal vectors is not zero");
        if (!isZero(v1.dotProduct(v2) + 28))
            fail("ERROR: dotProduct() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     * This test checks the scaling of a vector by a scalar.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(new Vector(2, 4, 6), new Vector(1, 2, 3).scale(2), "the method scale() is fail");

        // =============== Boundary Values Tests ==================
        // TC11: test scaling to 0
        assertThrows(IllegalArgumentException.class, () -> new Vector(1, 2, 3).scale(0d), "the method scale(0) must thrown Exception");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     * This test checks the calculation of the length of vectors.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(5d, new Vector(0, 3, 4).length(), "the method length() is fail");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     * This test checks the normalization of a vector.
     */
    @Test
    void testNormalize() {
        Vector v = new Vector(0, 3, 4);
        Vector n = v.normalize();
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(1d, n.lengthSquared(), 0.00001, "wrong normalized vector length");
        assertThrows(IllegalArgumentException.class, () -> v.crossProduct(n), "normalized vector is not in the same direction");
        assertEquals(new Vector(0, 0.6, 0.8), n, "wrong normalized vector");
    }
}
