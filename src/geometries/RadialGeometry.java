package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Abstract class representing radial geometries in the geometry system.
 * Radial geometries are geometries defined by a single radius parameter.
 */
public abstract class RadialGeometry implements Geometry {
    protected final double radius; // The radius of the radial geometry

    /**
     * Constructs a radial geometry with the given radius.
     *
     * @param radius the radius of the radial geometry
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }

    public abstract List<Point> findIntersections(Ray ray);
}
