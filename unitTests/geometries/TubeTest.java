package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in assertEquals
     */
    private static final double DELTA = 0.000001;

    @Test
   void testGetNormal() {
        // Create a tube with radius 1 and direction (1, 0, 0)
        Point p0 = new Point(0, 0, 0);
        Vector v = new Vector(1, 0, 0);
        Ray ray = new Ray(p0, v);
        Tube tube = new Tube(1, ray);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Check if the normal length is 1
        Point point1 = new Point(1, 1, 0);
        Vector normal1 = tube.getNormal(point1);
        assertEquals(1, normal1.length(), DELTA, "Wrong normal length");

        // TC02: Ensure the returned normal vector is correct
        Vector expectedNormal = new Vector(0,1,0);
        assertEquals(expectedNormal, normal1, "Wrong normal");

        // =============== Boundary Values Tests ==================

        // TC10: Test the case when (P - P0) is orthogonal to v
        Point point3 = new Point(1, 0, 1);
        Vector normal3 = tube.getNormal(point3);
        assertEquals(1, normal3.length(), DELTA, "Wrong normal length for orthogonal case");
        assertEquals(0, normal3.dotProduct(v), DELTA, "Normal is not orthogonal to the tube's direction");
    }

    @Test
    void testFindIntersections() {
        Tube tube = new Tube(1.0, new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)));

        // Test case where the ray does not intersect the tube
        Ray ray1 = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        List<Point> result1 = tube.findIntersections(ray1);
        assertNull(result1, "Ray does not intersect the tube");

        // Test case where the ray intersects the tube at two points
        Ray ray2 = new Ray(new Point(1, 0, 0), new Vector(0, 0, 1));
        List<Point> result2 = tube.findIntersections(ray2);
        assertEquals(2, result2.size(), "Ray should intersect the tube at two points");
        assertEquals(new Point(1, 0, 1), result2.get(0), "First intersection point is incorrect");
        assertEquals(new Point(1, 0, -1), result2.get(1), "Second intersection point is incorrect");

        // Test case where the ray intersects the tube at one point (tangent)
        Ray ray3 = new Ray(new Point(1, 1, 0), new Vector(0, -1, 0));
        List<Point> result3 = tube.findIntersections(ray3);
        assertEquals(1, result3.size(), "Ray should intersect the tube at one point");
        assertEquals(new Point(1, 0, 0), result3.get(0), "Intersection point is incorrect");
    }
}
