package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Represents a triangle geometry in the 3D space.
 * A triangle is a polygon with three edges and three vertices.
 */
public class Triangle extends Polygon {
    @Override
    public List<Point> findIntsersections(Ray ray) {
        return null;
    }
    // This class inherits Polygon's implementation for constructing and representing triangles.
}
