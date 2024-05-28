package primitives;

import geometries.Sphere;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link primitives.Point} class.
 * This class tests various methods related to the Point class, ensuring their correctness.
 */
class PointTests {
    Point p1 = new Point(1, 2, 3);

    /**
     * Test method for {@link primitives.Point#subtract(Point)}.
     * This test ensures that subtracting two points throws an exception if the resulting vector is zero.
     */
    @Test
    void testSubtract() {
        Point p2 = new Point(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p2),
                "Error: Point - Point does not throw an exception when resulting Vector(0,0,0)");
    }

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point)}.
     * This test checks the normal calculation at a given point on a sphere.
     */
    @Test
    void testGetNormal() {
        Sphere sph1 = new Sphere(Point.ZERO, 1);
        Point pt = new Point(0, 0, 10);
        assertEquals(new Vector(0, 0, 1), sph1.getNormal(pt),
                "Error: Sphere getNormal() does not return the correct normal");
    }

    /**
     * Test method for {@link primitives.Point#add(Vector)}.
     * This test combines multiple add tests to ensure point addition with vectors works correctly.
     */
    @Test
    void testAdd() {
        testAdd1();
        testAdd2();
    }

    /**
     * Test method for {@link primitives.Point#add(Vector)}.
     * This test checks the addition of a point and a vector.
     */
    @Test
    void testAdd1() {
        Vector v1 = new Vector(1, 2, 3);
        assertEquals(new Point(2, 4, 6), p1.add(v1),
                "Error: Point + Vector does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#add(Vector)}.
     * This test checks the addition of a point and a negative vector resulting in the zero point.
     */
    @Test
    void testAdd2() {
        Vector v1 = new Vector(-1, -2, -3);
        assertEquals(Point.ZERO, p1.add(v1),
                "Error: Point + Vector does not work correctly");
    }
}
