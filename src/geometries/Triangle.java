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
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        List<GeoPoint> intersec = plane.findGeoIntersections(ray);
        if (intersec == null){
            return null;
        }
        Point pointP0 = ray.getHead();
        Vector vectorDirect = ray.getDirection();

        Point point1 = vertices.get(0);
        Point point2 = vertices.get(1);
        Point point3 = vertices.get(2);

        Vector vector1 = point1.subtract(pointP0);
        Vector vector2 = point2.subtract(pointP0);
        Vector vector3 = point3.subtract(pointP0);

        Vector normal1 = vector1.crossProduct(vector2).normalize();
        Vector normal2 = vector2.crossProduct(vector3).normalize();
        Vector normal3 = vector3.crossProduct(vector1).normalize();


        double d1 = vectorDirect.dotProduct(normal1);
        double d2 = vectorDirect.dotProduct(normal2);
        double d3 = vectorDirect.dotProduct(normal3);

        if( (d1 > 0 && d2 > 0 && d3 > 0) || (d1 < 0 && d2 < 0 && d3 < 0) ) {
            return intersec;
        }

        return null;
    }


}
