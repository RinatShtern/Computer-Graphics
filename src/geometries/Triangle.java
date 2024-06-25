package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a triangle geometry in the 3D space.
 * A triangle is a polygon with three edges and three vertices.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle with the given vertices.
     *
     * @param p0 the first vertex of the triangle.
     * @param p1 the second vertex of the triangle.
     * @param p2 the third vertex of the triangle.
     */
    public Triangle(Point p0, Point p1, Point p2) {
        super(p0, p1, p2);
    }

    /**
     * Finds the intersection points between the triangle and a given ray, within a given distance.
     *
     * @param ray      the ray to check for intersections.
     * @param distance the maximum distance to check for intersections.
     * @return a list of intersection points with geometric context, or null if there are no intersections.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
        List<Point> intersections = plane.findIntersections(ray);
        // if there are no intersections with the plane, there are no intersections with the triangle
        if (intersections == null) {
            return null;
        }

        // if the ray intersects the plane at the triangle's plane
        Vector v1 = vertices.get(0).subtract(ray.getHead());
        Vector v2 = vertices.get(1).subtract(ray.getHead());
        Vector v3 = vertices.get(2).subtract(ray.getHead());

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double s1 = ray.getDirection().dotProduct(n1);
        double s2 = ray.getDirection().dotProduct(n2);
        double s3 = ray.getDirection().dotProduct(n3);

        // if the ray is parallel to the triangle's plane
        if (isZero(s1) || isZero(s2) || isZero(s3)) {
            return null;
        }

        // if the ray intersects the triangle
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            return List.of(new GeoPoint(this, intersections.get(0)));
        }

        // if the ray intersects the plane but not the triangle
        return null;
    }
}
