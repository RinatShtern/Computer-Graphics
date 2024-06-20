package primitives;

import geometries.Polygon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Vector class
 */
class VectorTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private final double DELTA = 0.000001;

    /** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test ctor based on coordinates
        assertDoesNotThrow(
                () -> new Vector(1, 2, 3),
                "Failed constructing a correct vector based on coordinates"
        );

        // TC02: test ctor based on type Double3
        assertDoesNotThrow(() -> new Vector(new Double3(4, 5, 6)),
                "Failed constructing a correct vector based on type Double3"
        );

        // =============== Boundary Values Tests ==================
        // TC11: illegal ZERO vector, based on coordinates
        assertThrows(
                IllegalArgumentException.class,
                () -> new Vector(0, 0, 0),
                "Failed - Zero vector constructed, by coords"
        );

        // TC12: illegal ZERO vector, based on Double3
        assertThrows(
                IllegalArgumentException.class,
                () -> new Vector(new Double3(0, 0, 0)),
                "Failed - Zero vector constructed, by Double3"
        );
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Vector vector = new Vector(1, 2, 2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: generic add test
        assertEquals(
                new Vector(3, 4, 3),
                vector.add(new Vector(2, 2, 1)),
                "ERROR: Vector + Vector does not work correctly"
        );

        // =============== Boundary Values Tests ==================
        // TC11: opposite add vector test
        assertThrows(
                IllegalArgumentException.class,
                () -> vector.add(new Vector(-1, -2, -2)),
                "ERROR: Vector + opposite itself does not build Zero vector"
        );
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        Vector vector = new Vector(1, 2, 7);

        // ============ Equivalence Partitions Tests ==============
        // TC01: generic scale test
        assertEquals(
                new Vector(4, 8, 28),
                vector.scale(4),
                "ERROR: scale wrong value"
        );

        // =============== Boundary Values Tests ==================
        // TC11: scale to zero
        assertThrows(
                IllegalArgumentException.class,
                () -> vector.scale(0),
                "ERROR: didn't build Zero vector when scale to Zero"
        );
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}.
     */
    @Test
    void testDotProduct() {
        Vector vector = new Vector(1, 2, 7);

        // ============ Equivalence Partitions Tests ==============
        // TC01: generic dot product test
        assertEquals(
                13,
                vector.dotProduct(new Vector(2, 2, 1)),
                DELTA,
                "ERROR: dotProduct() wrong value"
        );

        // =============== Boundary Values Tests ==================
        // TC11: orthogonal vectors dot product test (result 0)
        assertEquals(
                0,
                vector.dotProduct(new Vector(7, 0, -1)),
                DELTA,
                "ERROR: dotProduct() for orthogonal vectors is not zero"
        );
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        Vector vector1 = new Vector(1, 2, 3);
        Vector vector2 = new Vector(0, 3, -2);
        Vector crossVector = vector1.crossProduct(vector2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: generic cross product test (3 steps)
        // step1 confirm length
        assertEquals(
                vector1.length() * vector2.length(),
                crossVector.length(),
                DELTA,
                "length of cross product is not equal to multiplication between vectors' length"
        );

        // step2.1 confirm orthogonal
        assertEquals(
                0,
                crossVector.dotProduct(vector1),
                DELTA,
                "ERROR: crossProduct() result is not orthogonal to its left operands"
        );

        // step2.2 confirm orthogonal
        assertEquals(
                0,
                crossVector.dotProduct(vector2),
                DELTA,
                "ERROR: crossProduct() result is not orthogonal to its right operands"
        );

        // step3 correct unit vector
        assertEquals(
                new Vector(-13, 2, 3),
                crossVector,
                "ERROR: cross product result is incorrect"
        );

        // =============== Boundary Values Tests ==================
        // TC11: parallel vectors cross product test (result zero vector)
        assertThrows(
                IllegalArgumentException.class,
                () -> vector1.crossProduct(new Vector(2, 4, 6)),
                "ERROR: crossProduct() for parallel vectors does not result in Zero vector"
        );

        // TC12: opposite parallel vectors cross product test (result zero vector)
        assertThrows(
                IllegalArgumentException.class,
                () -> vector1.crossProduct(new Vector(-2, -4, -6)),
                "ERROR: crossProduct() for parallel vectors does not result in Zero vector"
        );
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        Vector vector = new Vector(1, 2, 2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: generic length squared test
        assertEquals(
                9,
                vector.lengthSquared(),
                DELTA,
                "ERROR: lengthSquared() wrong value"
        );
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        Vector vector = new Vector(1, 2, 2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: generic length test
        assertEquals(
                3,
                vector.length(),
                DELTA,
                "ERROR: length() wrong value"
        );
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        Vector vector = new Vector(1, 2, 3);
        Vector normalVector = vector.normalize();

        // ============ Equivalence Partitions Tests ==============
        // TC01: generic normalize test, 3 steps
        // step1 confirm unit vector
        assertEquals(
                1,
                normalVector.length(),
                DELTA,
                "ERROR: the normalized vector is not a unit vector"
        );

        // step2 confirm parallel to origin vector
        assertThrows(
                IllegalArgumentException.class,
                () -> vector.crossProduct(normalVector),
                "ERROR: the normalized vector is not parallel to the original one"
        );

        // step3 confirm same direction as origin vector.
        assertTrue(
                vector.dotProduct(normalVector) > 0,
                "ERROR: the normalized vector is opposite to the original one"
        );
    }
}
