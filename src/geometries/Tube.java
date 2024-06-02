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
        Vector p0_p = p.subtract(axis.getHead());
        double t = axis.getDirection().dotProduct(p0_p);
        Point o = axis.getHead().add(axis.getDirection().scale(t));

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
    public List<Point> findIntersections(Ray ray) {
        return List.of();
    }


    @Override
    public List<Point> findIntsersections(Ray ray) {
        return List.of();
    }
}
