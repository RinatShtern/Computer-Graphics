package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Sphere class
 */
class SphereTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        Point centerPoint = new Point(0, 2, 0);
        Sphere sp = new Sphere(centerPoint, 4);
        Vector radiusVector = new Vector(0, 0, 4);

        // ============ Equivalence Partitions Tests ==============
        // Test if the normal is correct
        Vector expectedNormal = new Vector(0, 0, 1);
        Point pointOnSurface = new Point(0, 2, 4);

        Vector normal = sp.getNormal(pointOnSurface);
        assertEquals(expectedNormal, normal, "Sphere's normal is not correct");

        // Test if the normal is a unit vector
        assertEquals(1, normal.length(), DELTA, "Sphere's normal is not a unit vector");

        // Test if the normal is orthogonal to the radius vector
        assertEquals(0, normal.dotProduct(radiusVector), DELTA, "Sphere's normal is not orthogonal to the radius vector");
    }
}
