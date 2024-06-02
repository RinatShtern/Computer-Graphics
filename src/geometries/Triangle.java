package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents a triangle geometry in the 3D space.
 * A triangle is a polygon with three edges and three vertices.
 */
public class Triangle extends Polygon {
    public Triangle(Point p0,Point p1,Point p2){
        super(p0,p1,p2);
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return super.findIntersections(ray);
    }


    // This class inherits Polygon's implementation for constructing and representing triangles.
}
