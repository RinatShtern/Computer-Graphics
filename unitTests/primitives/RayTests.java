package primitives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RayTests {

    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: point is in the middle of the ray
        Ray r1 = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        double sqrt3 = Math.sqrt(3);
        Point expected1 = new Point(1 + 1/sqrt3, 1 + 1/sqrt3, 1 + 1/sqrt3);
        assertEquals(expected1, r1.getPoint(1), "Bad point");

        // TC02: point is not in the middle of the ray
        Point expected2=new Point(1, 1, 1);
        Ray r2 = new Ray(expected2, new Vector(1, 1, 1));
        assertEquals(new Point(1 + 2/sqrt3, 1 + 2/sqrt3, 1 + 2/sqrt3), r2.getPoint(2), "Bad point");

        // =============== Boundary Values Tests ==================
        // TC03: point is at the beginning of the ray
        Point expected3 =new Point(1, 1, 1);
        Ray r3 = new Ray(new Point(1, 1, 1), new Vector(1, 1, 1));
        assertEquals(expected3, r3.getPoint(0), "Bad point");
    }
}
