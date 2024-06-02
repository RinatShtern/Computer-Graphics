package geometries;


import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class GeometriesTests {
    @Test
    void testFindIntersections() {
        Geometries geometries = new Geometries();

        // Test case where no geometries are added
        Ray ray1 = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        assertNull(geometries.findIntersections(ray1), "No geometries - should return null");

        // Add some geometries
        Sphere sphere = new Sphere(new Point(0, 0, 0),1.0);
        Plane plane = new Plane(new Vector(0, 0, 1), new Point(0, 0, 1));
        geometries.add(sphere, plane);

        // Test case where the ray intersects the sphere and the plane
        Ray ray2 = new Ray(new Point(0, 0, -3), new Vector(0, 0, 1));
        List<Point> result2 = geometries.findIntersections(ray2);
        assertNotNull(result2, "Ray intersects geometries - should not return null");
        assertEquals(3, result2.size(), "Should find three intersection points");

        // Test case where the ray intersects none of the geometries
        Ray ray3 = new Ray(new Point(2, 2, 2), new Vector(1, 1, 1));
        assertNull(geometries.findIntersections(ray3), "Ray intersects no geometries - should return null");
    }
    @Test
    public void testAddEmpty() {
        // BVA - מקרה קצה עליון תחתון
        Geometries geometries = new Geometries();
        assertTrue(geometries.getIntersectables().isEmpty(), "The list should be empty.");
    }
}