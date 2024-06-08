package geometries;

import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import java.util.Collection;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a plane geometry in the 3D space.
 * A plane is defined by a point in the plane and a normal vector to the plane.
 */
public class Plane implements Geometry {
    private final Point q; // A point on the plane
    private final Vector normal; // The normal vector to the plane

    /**
     * Constructs a plane from a normal vector and a point on the plane.
     *
     * @param normal the normal vector to the plane
     * @param q      a point on the plane
     */
    public Plane(Vector normal, Point q) {
        this.normal = normal.normalize();
        this.q = q;
    }

    /**
     * Constructs a plane from three points.
     *
     * @param p1 the first point
     * @param p2 the second point
     * @param p3 the third point
     */
    public Plane(Point p1, Point p2, Point p3) {
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        Vector n  = v1.crossProduct(v2); // Calculation of normal vector to be implemented
        normal = n.normalize();
        this.q = p1;
    }

    /**
     * Retrieves the normal vector to the plane at a given point.
     *
     * @param p the point on the plane (not used in this implementation)
     * @return the normal vector to the plane
     */
    @Override
    public Vector getNormal(Point p) {
        return normal;
    }

    /**
     * Retrieves the normal vector to the plane.
     *
     * @return the normal vector to the plane
     */
    public Vector getNormal() {
        return normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();
        Vector n = normal;

        double nv = alignZero(n.dotProduct(v));

        //if ray is parallel to plane - no intersection points
        if (isZero(nv)) {
            return null;
        }

        // Check if the ray starts at the reference point of the plane
        if (p0.equals(q)) {
            // If the direction vector is also in the plane (dot product is zero), no intersection
            if (isZero(n.dotProduct(v))) {
                return null;
            } else {
                // If the direction vector is not zero, return the starting point
                return List.of(p0);
            }
        }

        //Vector P0_Q = p0.subtract(q);
        Vector P0_Q = q.subtract(ray.getHead());

        double t = alignZero(n.dotProduct(P0_Q) / nv);

        //if ( t == 0) origin of ray lay on the plane
        if (isZero(t)) {
            return null;
        }

        // if (t < 0) the direction of the ray points in the opposite direction
        if (t > 0) {
          //  Point P = p0.add(v.scale(t));
            Point P = ray.getPoint(t); // Using getPoint to find the intersection point
            return List.of(P);
        }
        return null;
    }
}
