package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTests {

    @Test
    public void testGetNormalPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Plane pl = new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point(0, 0, 1)), "Bad normal to plane");
        Point p3=new Point(0,0,0);
        Point p4=new Point(0,0,1);
        Point p5=new Point(0,4,2);

        // =============== Boundary Values Tests ==================
        try {
            new Plane(p3,p5,p3);
        }
        catch (IllegalArgumentException e){
            fail("the constructor of plane get same points");
        }

        // =============== Boundary Values Tests ==================
        try {
            new Plane(p3,p4,p5);
        }
        catch (IllegalArgumentException e){
            fail("there is two points are on the same line");
        }

    }
}