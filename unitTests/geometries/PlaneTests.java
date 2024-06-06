package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
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
    public void testFindIntersections() {
        Plane pl = new Plane(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));

        // ============ Equivalence Partitions Tests ==============

        // **** Group: The Ray must be neither orthogonal nor parallel to the plane
        // TC01: The Ray intersects the plane
        assertEquals(1, (pl.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 4, 5)))).size(), "Wrong number of points");

        // TC02: Ray does not intersect the plane
        assertNull((pl.findIntersections(new Ray(new Point(1, 0, 0), new Vector(5, 0, 0)))), "Wrong number of points");

        // =============== Boundary Values Tests ==================

        // **** Group: Ray is parallel to the plane
        // TC03: The ray included in the plane
        assertNull((pl.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 4, 0)))), "Wrong number of points");

        // TC04: The ray not included in the plane
        assertNull((pl.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 4, 0)))), "Wrong number of points");

        // **** Group: Ray is orthogonal to the plane
        // TC05: The Ray before the plane
        assertEquals(1, (pl.findIntersections(new Ray(new Point(0, 0, 0.5), new Vector(0, 0, 1)))).size(), "Wrong number of points");

        // TC06: The Ray in the plane
        assertEquals(1, (pl.findIntersections(new Ray(new Point(0, 0, -2), new Vector(0, 0, 4)))).size(), "Wrong number of points");

        // TC07: The Ray after the plane
        assertNull((pl.findIntersections(new Ray(new Point(0, 0, 1.5), new Vector(0, 0, 10)))), "Wrong number of points");

        // **** Group: Ray is neither orthogonal nor parallel to and begins at the plane
        // TC08: Ray begins at the plane and goes in a direction that does not intersect the plane
        assertNull((pl.findIntersections(new Ray(new Point(0, 2, 1), new Vector(2, 0, -4)))), "Wrong number of points");

        // **** Group: Ray is neither orthogonal nor parallel to and begins at the same point which appears as reference point in the plane
        // TC09: Ray begins at a reference point on the plane and intersects the plane
        assertEquals(1, (pl.findIntersections(new Ray(new Point(0, 0, 1), new Vector(2, 2, -1))).size()), "Wrong number of points");
    }

}
