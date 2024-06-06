package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTests {

    @Test
    void testGetPoint() {
// ============ Equivalence Partitions Tests ==============
        // TC01: point is in the middle of the ray
        Ray r1 = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        assertEquals(new Point(2, 2, 2), r1.getPoint(1), "Bad point");

        // TC02: point is not in the middle of the ray
        Ray r2 = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        assertEquals(new Point(3, 3, 3), r2.getPoint(2), "Bad point");

        // =============== Boundary Values Tests ==================
        // TC03: point is at the beginning of the ray
        Ray r3 = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        assertEquals(new Point(1, 1, 1), r3.getPoint(0), "Bad point");
    }
}