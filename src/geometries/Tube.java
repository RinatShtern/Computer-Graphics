package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a tube geometry in the 3D space.
 * A tube is a cylindrical geometry with no end caps, defined by its radius and axis.
 */
public class Tube extends RadialGeometry {
    protected Ray axis; // The axis of the tube

    /**
     * Constructs a tube with the given radius.
     * @param radius the radius of the tube
     * @param ray the ray of the tube
     */
    public Tube(double radius,Ray ray) {
        super(radius);
        if (radius <= 0)
            throw new IllegalArgumentException("radius must be positive value");
        axis = ray;
    }

    /**
     * Retrieves the normal vector to the tube at a given point.
     *
     * @param p the point on the tube
     * @return the normal vector to the tube at the given point
     */
    @Override
    public Vector getNormal(Point p) {

        //  Point o = axis.getHead().add(axis.getDirection().scale(t));
        Vector p0_p = p.subtract(axis.getHead());
        double t = axis.getDirection().dotProduct(p0_p);
        Point o = axis.getPoint(t); // Using getPoint

        //given point is on axis ray
        if (p.equals(o))
            throw new IllegalArgumentException("point cannot be on the axis ray");

        // point is against tube origin point
        if (isZero(t))
            return p.subtract(axis.getHead()).normalize();
        // any other point
        else
            return p.subtract(o).normalize();

    }
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double distance) {
        // Calculate the projection of the ray's head onto the tube's axis
        double t = axis.getDirection().dotProduct(ray.getDirection());
        Vector v;
        try {
            v = ray.getHead().subtract(axis.getPoint(t)); // Using getPoint
            //v = ray.getHead().subtract(axis.getHead().add(axis.getDirection().scale(t)));
        } catch (IllegalArgumentException e) {
            // The ray's head is on the axis
            v = ray.getHead().subtract(axis.getHead());
        }

        double a = ray.getDirection().lengthSquared();
        double b = 2 * ray.getDirection().dotProduct(v);
        double c = v.lengthSquared() - radius * radius;

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            // No intersections
            return null;
        } else if (isZero(discriminant)) {
            // One intersection
            double t1 = -b / (2 * a);
            return List.of(new GeoPoint(this,ray.getPoint(t1)));
        } else {
            // Two intersections
            double sqrtDiscriminant = Math.sqrt(discriminant);
            double t1 = (-b + sqrtDiscriminant) / (2 * a);
            double t2 = (-b - sqrtDiscriminant) / (2 * a);
            return List.of(new GeoPoint(this,ray.getPoint(t1)), new GeoPoint(this,ray.getPoint(t2)));
        }
    }


}
