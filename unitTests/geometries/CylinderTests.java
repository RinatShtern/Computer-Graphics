package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class CylinderTests {
    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Partitions Tests ==============
        Point p = new Point(1,0,1);
        Vector expectedNormal = new Vector(0,1,0);
        Ray ray = new Ray(p,expectedNormal);
        Cylinder cylinder = new Cylinder(1,ray,2);
        Vector actualNormal =cylinder.getNormal(p);
        assertEquals(expectedNormal,actualNormal,
                "ERROR: getNormal() douse not return the correct normal");


    }
}
