package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

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
     * @param ray the ray of the cilinder
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray ray, double height) {
        super(radius,ray);
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
        Point p0 = axis.getHead();//The point is on the bottom
        Vector vector, vector1;
        Point point1;
        // The point is exactly in the center.
        if (p.equals(p0)) {
            return axis.getDirection().scale(-1);
        }
        vector = p0.subtract(p);
        // The point on the bottom but not in the center
        if (isZero(vector.dotProduct(axis.getDirection()))) {
            return axis.getDirection().scale(-1);
        }

        point1 = p0.add(axis.getDirection().scale(height));

        // The point is exactly in the center.
        if (p.equals(point1)) {
            return axis.getDirection();
        }
        vector1 = point1.subtract(p);
        // The point on the top but not in the center
        if (isZero(vector1.dotProduct(axis.getDirection()))) {
            return axis.getDirection();
        }
        //The point on the side, handle it like a tube.
        return super.getNormal(p);
    }

}
