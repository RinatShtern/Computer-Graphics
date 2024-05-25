package primitives;

import geometries.Polygon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VectorTests {

    private final double DELTA = 0.000001;

    /** Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}. */
    @Test
    public void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v123 = new Vector(0,0,1);
        Vector v03M2 = new Vector(1,0,0);
        Vector vr =v123.crossProduct(v03M2);

        assertEquals(0,vr.dotProduct(v123));
        assertEquals(0,vr.dotProduct(v03M2));

        Vector vM2M4M6 = new Vector(0,0,30);
    }
    @Test
    void testAdd(){
        Vector v1 = new Vector(1.0,  2.0, 3.0);
        Vector v2 = new Vector(4.0, 5.0, 6.0);

        Vector result = v1.add(v2);

        assertEquals(new Vector( 5.0,  7.0,  9.0), result);
    }

}
