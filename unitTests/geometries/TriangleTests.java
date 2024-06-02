package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Triangle class
 */
class TriangleTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     * */
    @Test
    void testGetNormal() {
        // Define new points for the triangle
        Point point = new Point(1, 1, 1);
        // Define expected normal vectors
        Vector expectedNormal1 = new Vector(0, 0, 1); // For a triangle in the XY plane
        Vector expectedNormal2 = new Vector(1, 1, 1); // For an equilateral triangle

        // Test for a triangle lying in the XY plane
        Triangle tr1 = new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );
        assertEquals(expectedNormal1, tr1.getNormal(point), "Triangle's normal is not correct");

        // Test for an equilateral triangle
        Triangle tr2 = new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0.5, Math.sqrt(3) / 2, 0)
        );
        assertEquals(expectedNormal2, tr2.getNormal(point), "Triangle's normal is not correct");
    }
}
