package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents a tube geometry in the 3D space.
 * A tube is a cylindrical geometry with no end caps, defined by its radius and axis.
 */
public class Tube extends RadialGeometry {
    protected Ray axis; // The axis of the tube

    /**
     * Constructs a tube with the given radius.
     *
     * @param radius the radius of the tube
     */
    public Tube(double radius) {
        super(radius);
    }

    /**
     * Retrieves the normal vector to the tube at a given point.
     *
     * @param p the point on the tube
     * @return the normal vector to the tube at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return null; // Not implemented yet
    }
}
