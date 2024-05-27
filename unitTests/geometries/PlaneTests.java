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
        Point p1=new Point(0,0,1);
        Point p2=new Point(1,0,0);
        Point p3=new Point(0,1,0);
        Plane pl = new Plane(p1, p2, p3);
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point(0, 0, 1)), "Bad normal to plane");
        Point p4=new Point(0,0,0);
        Point p5=new Point(0,0,1);
        Point p6=new Point(0,4,2);
        // =============== Boundary Values Tests ==================
        try {
            new Plane(p4,p5,p6);
        }
        catch (IllegalArgumentException e){
            fail("there is two points on the same line");
        }
        Vector normal = pl.getNormal();
        assertEquals(1,normal.length(),"Normal vector should have unit length");
        Vector vector1 =p2.subtract(p1);
        Vector vector2=p3.subtract(p1);

        //=============== Willingness Partitions Tests ================
        double dotProdect = normal.dotProduct(vector1);
        assertEquals(0, dotProdect, "Normal vector is not prependicular to the plane");
        dotProdect = normal.dotProduct(vector2);
        assertEquals(0, dotProdect, "Normal vector is not prependicular to the plane");
        //assertEquals also can be assertTree

    }
}