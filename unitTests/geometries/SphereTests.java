package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class SphereTests {

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: a simple test
        Sphere s = new Sphere(Point.ZERO, 2);
        assertEquals(new Vector(1, 0, 0),
                s.getNormal(new Point(1, 0, 0)),
                "ERROR: getNormal() is wrong");
    }
}