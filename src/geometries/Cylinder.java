package geometries;

import primitives.Point;
import primitives.Ray;
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
     * @param ray the ray of the cilinder
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
        Vector vTop = p.subtract(new Point(0,0,0));
        double pointHeight = vTop.dotProduct(new Vector(0,1,0));

        return vTop.subtract(new Vector(0,pointHeight,0)).normalize();

    }

}
