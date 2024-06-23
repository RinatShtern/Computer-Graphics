package geometries;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * The Geometry interface represents geometric objects in the 3D space.
 * Implementing classes should provide methods for calculating normal vectors at given points.
 */
public abstract class Geometry extends Intersectable
{
    private Color emission=Color.BLACK;
    /**
     * Calculates the normal vector to the geometry at the specified point.
     *
     * @param p the point on the geometry
     * @return the normal vector to the geometry at the specified point
     */
   public abstract Vector getNormal(Point p);

    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
}
