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
    @Test
    public void testFindIntersections () {
        Plane pl = new Plane(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));
        // ============ Equivalence Partitions Tests ==============
        //**** Group: The Ray must be neither orthogonal nor parallel to the plane
        //TC01:The Ray intersects the plane
        assertEquals( 1, (pl.findIntersections(new Ray(new Point(0,0,1),new Vector(0,0,5)))).size(),"Wrong number of points");

        //TC02:Ray does not intersect the plane
        assertEquals( 0, (pl.findIntersections(new Ray(new Point(1,0,0),new Vector(5,0,0)))).size(),"Wrong number of points");

        // =============== Boundary Values Tests ==================

        // **** Group: Ray is parallel to the plane
        //TC03:the ray included in the plane
        assertEquals( 0, (pl.findIntersections(new Ray(new Point(0,0,1),new Vector(0,4,0)))).size(),"Wrong number of points");

        //TC04:the ray not included in the plane
        assertEquals( 0, (pl.findIntersections(new Ray(new Point(1,0,0),new Vector(0,4,0)))).size(),"Wrong number of points");

        //****Group: Ray is orthogonal to the plane
        //Tc05:The Ray before the plane
        assertEquals( 1, (pl.findIntersections(new Ray(new Point(0,0,0.5),new Vector(0,0,1)))).size(),"Wrong number of points");

        //Tc06:The Ray in the plane
        assertEquals( 1, (pl.findIntersections(new Ray(new Point(0,0,1),new Vector(0,0,4)))).size(),"Wrong number of points");

        //Tc07:The Ray after the plane
        assertEquals( 0, (pl.findIntersections(new Ray(new Point(0,0,1.5),new Vector(0,0,10)))).size(),"Wrong number of points");

        //TC08: Ray is neither orthogonal nor parallel to and begins at the plane
        assertEquals( 0, (pl.findIntersections(new Ray(new Point(0,2,1),new Vector(2,0,-4)))).size(),"Wrong number of points");

        //TC08: Ray is neither orthogonal nor parallel to and begins at the same point which appears as reference point in the plane
        assertEquals( 0, (pl.findIntersections(new Ray(new Point(0,0,1),new Vector(2,0,-4)))).size(),"Wrong number of points");

    }


}
