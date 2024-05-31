package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents a triangle geometry in the 3D space.
 * A triangle is a polygon with three edges and three vertices.
 */
public class Triangle implements Geometry {
    @Override
    public List<Point> findIntsersections(Ray ray) {
        return null;
    }

    @Override
    public Vector getNormal(Point p) {
        return null;
    }
    // This class inherits Polygon's implementation for constructing and representing triangles.
}
