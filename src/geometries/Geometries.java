package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;

public class Geometries implements Intersectable{
    final private List<Intersectable> geometries = new LinkedList<>();;

    public Geometries(){}

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries,geometries);
    }

    /**
     * Finds the intersection points between a ray and the geometries.
     *
     * @param ray the ray to intersect with
     * @return list of intersection points, or null if no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersectionPoints = null;

        for (Intersectable geometry : geometries) {
            List<Point> geometryPoints = geometry.findIntersections(ray);
            if (geometryPoints != null) {
                if (intersectionPoints == null) {
                    intersectionPoints = new LinkedList<>();
                }
                intersectionPoints.addAll(geometryPoints);
            }
        }

        return intersectionPoints;
    }
    public List<Point> intersectionPoints(Ray ray) {
        List<Point> intersectionPoints = null;
        List<Point> geometryPoints = List.of();
        for (Intersectable geometry : this.geometries) {
            geometryPoints = geometry.findIntersections(ray);
            if (geometryPoints != null) {
                if (intersectionPoints == null) {
                    intersectionPoints = new LinkedList<>();
                }
                intersectionPoints.addAll(geometryPoints);
            }
        }
        if(intersectionPoints == null)
            return null;

        return intersectionPoints
                .stream()
                .sorted()
                .toList();
    }

    public CharSequence getIntersectables() {
        return this.geometries.toString();
    }


}