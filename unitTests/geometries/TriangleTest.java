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
        double coord = Math.sqrt(1d/3);
        double oppCoord = -1 * coord;
        Point point = new Point(0.43,0.25,0.33);
        Vector normal = new Vector(coord,coord,coord);
        Vector opNormal = new Vector(oppCoord,oppCoord,oppCoord);
        Triangle tr = new Triangle(
                new Point(0,0,1),
                new Point(1,0,0),
                new Point(0,1,0)
        );

        // ============ Equivalence Partitions Tests ==============
        // generic normal to Triangle test, 3 steps
        // test normal is correct
        assertTrue(
                normal.equals(tr.getNormal(point))
                        || opNormal.equals(tr.getNormal(point)),
                "Triangle's normal is not correct"
        );

        // test unit normal
        // (the new Point is in the triangle)
        assertEquals(
                1,
                tr.getNormal(point).length(),
                DELTA,
                "Triangle's normal is not a unit vector"
        );

        // test orthogonal
        // (the new Point and new Vectors are in the triangle)
        assertEquals(
                0,
                tr.getNormal(point).dotProduct(new Vector(1,-1,0)),
                DELTA,
                "Triangle's normal is not orthogonal 1"
        );

        assertEquals(
                0,
                tr.getNormal(point).dotProduct(new Vector(0,-1,1)),
                DELTA,
                "Triangle's normal is not orthogonal 2"
        );
    }
}