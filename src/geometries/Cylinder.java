package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a cylinder in the geometry system.
 * A cylinder is a geometric shape where the density is constant along its length and the cross-sections are circles.
 * The cylinder, according to physical principles, consists of a cylinder and different bases on each side.
 */
public class Cylinder extends Tube {
    private final double height; // The height of the cylinder

    /**
     * Constructs a new cylinder.
     *
     * @param radius the radius of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, double height) {
        super(radius);
        this.height = height;
    }

    /**
     * Retrieves the normal vector to the cylinder at a given point.
     *
     * @param p the point on the cylinder
     * @return the normal vector to the cylinder at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return null; // Not implemented yet
    }
}
