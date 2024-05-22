package primitives;

import geometries.Sphere;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTests {
    Point p1 =new Point(1,2,3);
    @Test
    void testSubtract(){
        Point p2 = new Point(1,2,3);
        assertThrows(IllegalArgumentException.class,()->p1.subtract(p2),"Error: Point - Point does not throw an exception when resulting Vector(0,0,0)");
    }
    @Test
    void testGetNormal(){
        Sphere sph1 = new Sphere(Point.ZERO,1);
        Point pt = new Point(0,0,10);

        assertEquals(new Vector(0,0,1),sph1.getNormal(pt));
    }
    @Test
    void testAdd(){
        testAdd1();
        testAdd2();
    }
    @Test
    void testAdd1(){
        Vector v1 = new Vector(1,2,3);

        assertEquals(new Point(2,4,6),p1.add(v1),"Error:Point + Vector does not work correctly");
    }
    @Test
    void testAdd2(){
        Vector v1 = new Vector(-1,-2,-3);

        assertEquals(Point.ZERO,p1.add(v1),"Error:Point + Vector does not work correctly");
    }

}