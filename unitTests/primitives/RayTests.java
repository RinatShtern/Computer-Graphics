package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

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
    /**
     * Test method for{@link Ray#(List)}
     */
    @Test
    void findClosestPoint() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 10, -100));

        // ============ Equivalence Partitions Tests ==============
        // TC01: The nearest point is in the middle of the list
        List<Point> list1 = List.of(
                new Point(0, 0, 2),
                new Point(0, 0, 1),
                new Point(10, 3, 1));

        assertEquals(new Point(0, 0, 1), ray.findClosestPoint(list1));


        // =============== Boundary Values Tests ==================
        // TC11: The list is null
        List<Point> list2 = null;

        assertNull(ray.findClosestPoint(list2));

        // TC12: The nearest point is the first point of the list
        List<Point> list3 = List.of(
                new Point(0, 0, 1),
                new Point(0, 0, 2),
                new Point(10, 3, 1));

        assertEquals(new Point(0, 0, 1), ray.findClosestPoint(list3));

        // TC13: The nearest point is last point of the list
        List<Point> list4 = List.of(
                new Point(0, 0, 2),
                new Point(10, 3, 1),
                new Point(0, 0, 1));

        assertEquals(new Point(0, 0, 1), ray.findClosestPoint(list4));
    }
}
