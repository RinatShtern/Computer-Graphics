package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * The Geometry interface represents geometric objects in the 3D space.
 * Implementing classes should provide methods for calculating normal vectors at given points.
 */
public interface Geometry extends Intersectable {

    /**
     * Calculates the normal vector to the geometry at the specified point.
     *
     * @param p the point on the geometry
     * @return the normal vector to the geometry at the specified point
     */
    Vector getNormal(Point p);
}
