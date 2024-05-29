package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Cylinder#getNormal(primitives.Point)} method.
 * This class tests the correctness of the normal vector calculation at various points on the cylinder.
 */
public class CylinderTests {

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     * This method tests the following cases:
     * <ul>
     *     <li>Point at the bottom center of the cylinder</li>
     *     <li>Point on the bottom base but not at the center</li>
     *     <li>Point at the top center of the cylinder</li>
     *     <li>Point on the top base but not at the center</li>
     *     <li>Point on the curved side surface of the cylinder</li>
     * </ul>
     */
    @Test
    void testGetNormal() {
        // Define the cylinder's axis
        Point p0 = new Point(0, 0, 0); // נקודת ההתחלה של ציר הצילינדר
        Vector dir = new Vector(0, 0, 1); // וקטור הכיוון של הצילינדר
        Ray axisRay = new Ray(p0, dir); // ריי המייצג את ציר הצילינדר
        double height = 10.0; // גובה הצילינדר
        double radius = 1.0; // רדיוס הצילינדר
        Cylinder cylinder = new Cylinder(radius, axisRay, height); // יצירת אובייקט צילינדר

        // Test 1: Point at the bottom center
        Point pointBottomCenter = new Point(0, 0, 0);
        Vector expectedNormalBottomCenter = new Vector(0, 0, -1); // הנורמל הצפוי במרכז הבסיס התחתון
        assertEquals(expectedNormalBottomCenter, cylinder.getNormal(pointBottomCenter),
                "ERROR: getNormal() does not return the correct normal for bottom center");

        // Test 2: Point on the bottom base but not at the center
        Point pointBottomEdge = new Point(1, 0, 0);
        Vector expectedNormalBottomEdge = new Vector(0, 0, -1); // הנורמל הצפוי בקצה הבסיס התחתון
        assertEquals(expectedNormalBottomEdge, cylinder.getNormal(pointBottomEdge),
                "ERROR: getNormal() does not return the correct normal for bottom edge");

        // Test 3: Point at the top center
        Point pointTopCenter = new Point(0, 0, 10);
        Vector expectedNormalTopCenter = new Vector(0, 0, 1); // הנורמל הצפוי במרכז הבסיס העליון
        assertEquals(expectedNormalTopCenter, cylinder.getNormal(pointTopCenter),
                "ERROR: getNormal() does not return the correct normal for top center");

        // Test 4: Point on the top base but not at the center
        Point pointTopEdge = new Point(1, 0, 10);
        Vector expectedNormalTopEdge = new Vector(0, 0, 1); // הנורמל הצפוי בקצה הבסיס העליון
        assertEquals(expectedNormalTopEdge, cylinder.getNormal(pointTopEdge),
                "ERROR: getNormal() does not return the correct normal for top edge");

        // Test 5: Point on the curved side surface of the cylinder
        Point pointSide = new Point(1, 0, 5);
        Vector expectedNormalSide = new Vector(1, 0, 0); // הנורמל הצפוי על המשטח הצדדי
        assertEquals(expectedNormalSide, cylinder.getNormal(pointSide),
                "ERROR: getNormal() does not return the correct normal for side surface");
    }
    @Test
    void findIntsersections(){}

}
