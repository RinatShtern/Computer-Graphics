package geometries;

import primitives.*;
import primitives.Point;

import java.util.List;


/**
 * An interface for intersectable geometries.
 */
public interface Intersectable {
    /**
     * Finds the intersections between a ray and the geometry.
     *
     * @param ray the ray to intersect with
     * @return list of intersection points, or null if no intersections
     */
    List<Point> findIntersections(Ray ray);
}