package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Plane#getNormal(Point)} method.
 * This class tests the correctness of the normal vector calculation for a plane at various points.
 */
class PlaneTests {

    /**
     * Test method for {@link geometries.Plane#getNormal(Point)}.
     * This method tests the following cases:
     * <ul>
     *     <li>Simple test to check normal vector calculation for a plane defined by three points</li>
     *     <li>Boundary test to check that an exception is thrown when two points are on the same line</li>
     *     <li>Check that the normal vector has unit length</li>
     *     <li>Check that the normal vector is perpendicular to vectors on the plane</li>
     * </ul>
     */
    @Test
    public void testGetNormalPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test to check normal vector calculation for a plane defined by three points
        Point p1 = new Point(0, 0, 1);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane pl = new Plane(p1, p2, p3);
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point(0, 0, 1)), "Bad normal to plane");

        // =============== Boundary Values Tests ==================
        // TC02: Boundary test to check that an exception is thrown when two points are on the same line
        Point p4 = new Point(0, 0, 0);
        Point p5 = new Point(0, 0, 1);
        Point p6 = new Point(0, 4, 2);
        try {
            new Plane(p4, p5, p6);
        } catch (IllegalArgumentException e) {
            fail("there are two points on the same line");
        }

        // Check that the normal vector has unit length
        Vector normal = pl.getNormal();
        assertEquals(1, normal.length(), "Normal vector should have unit length");

        // Check that the normal vector is perpendicular to vectors on the plane
        Vector vector1 = p2.subtract(p1);
        Vector vector2 = p3.subtract(p1);

        // Check that the dot product of the normal vector with vectors on the plane is zero
        double dotProduct = normal.dotProduct(vector1);
        assertEquals(0, dotProduct, "Normal vector is not perpendicular to the plane");
        dotProduct = normal.dotProduct(vector2);
        assertEquals(0, dotProduct, "Normal vector is not perpendicular to the plane");
    }
}
