package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
        Vector expectedNormal2 = new Vector(0, 0, 1); // For an equilateral triangle

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

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        final Point p100 = new Point(1,0,0);
        final Point p030 = new Point(0,3,0);
        final Point p520 = new Point(5,2,0);
        Triangle triangle = new Triangle(p100,p030,p520);

        // ============ Equivalence Partitions Tests ==============
        Point p001 = new Point(0,0,1);
        var result = triangle.findIntersections(new Ray(p001, new Vector(2,1,-1)));

        // TC01: Point is in the triangle (2 steps)
        //step 1 - confirm only one intersection point
        assertEquals(1,
                result.size(),
                "Wrong amount of points TC01");
        //step 2 - confirm correct intersection point
        assertEquals(new Point(2,1,0),
                result.getFirst(),
                "Wrong intersection point TC01");

        // TC02: Point against edge of triangle
        assertNull(triangle.findIntersections(new Ray(p001, new Vector(0,2,-1))),
                "No intersecting point but found one - edge TC02");

        // TC03: Point against corner of triangle
        assertNull(triangle.findIntersections(new Ray(p001, new Vector(10,2,-1))),
                "No intersecting point but found one - corner TC03");

        // =============== Boundary Values Tests ==================
        // TC11: Point on edge of triangle
        assertNull(triangle.findIntersections(new Ray(p001, new Vector(2.6,0.8,-1))),
                "No intersecting point but found one - edge TC11");

        // TC12: Point on continuation of edge
        assertNull(triangle.findIntersections(new Ray(p001, new Vector(8,3.5,0))),
                "No intersecting point but found one - edge TC12");

        // TC13: Point on corner of triangle
        assertNull(triangle.findIntersections(new Ray(p001, new Vector(1,0,-1))),
                "No intersecting point but found one - corner TC13");
    }


}
