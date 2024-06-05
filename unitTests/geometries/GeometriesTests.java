package geometries;

import primitives.Point;
import primitives.Ray;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.Vector;


class GeometriesTests {
    @Test
    void testFindIntersections() {
        Geometries geometries = new Geometries();
        Vector v1 = new Vector(1, 0, 0);
        Ray ray = new Ray(new Point(-1, 0, 1), v1);

        // ============ Boundary Values Tests ==============
        //TC01: empty list
        assertNull(geometries.findIntersections(ray), "No geometries - should return null");
        //TC02: No shape has intersections with the Ray
        Triangle onSideTriangle = new Triangle(new Point(1, 1, 1), new Point(1, 1, 0), new Point(1, 2, 0));
        Point p = new Point(0, -2, 0);
        Sphere onSideSphere = new Sphere(p, 1);
        Plane flatPlane = new Plane(new Vector(0, 0, 1),new Point(1, 1, 1));


        geometries.add(onSideSphere, onSideTriangle, flatPlane);
        assertNull(geometries.findIntersections(ray),
                "wrong result when no shape has intersections with the Ray- expected to 0 points");

        //TC03: only one shape has intersections with the ray (triangle)
        Geometries geometries_temp = new Geometries();
        Vector v2 = new Vector(1, 0, 1);
        Ray ray2 = new Ray(new Point(-1, 3, -1.5), v2);
        Triangle onCenterTriangle = new Triangle(new Point(1, 0, 2), new Point(1, 1, 0), new Point(1, 7, 0));
        geometries.add(onCenterTriangle);
        assertEquals(1, geometries_temp.findIntersections(ray2).size(),
                "wrong result: expected to find 1 point");

        //TC04: all the shapes have intersections with the ray (triangle, plane and sphere)
        Plane plane = new Plane( new Vector(1, 0, 0),new Point(3, 0, 0));
        Sphere onCenterSphere = new Sphere(p, 2); //geometries2 has only 2 shapes on center
        Geometries geometries2 = new Geometries(onCenterSphere, onCenterTriangle, plane);
        assertEquals(4, geometries2.findIntersections(ray).size(),
                "wrong result: expected to find 4 points");

        // ============ Equivalence Partitions Tests ==============
        geometries.add(onCenterSphere); //geometries has 2 shapes on sides and 2 shapes on center
        assertEquals(3, geometries.findIntersections(ray).size(),
                "wrong result: expected to find 3 points");
    }

}

