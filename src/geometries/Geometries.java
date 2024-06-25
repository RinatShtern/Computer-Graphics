package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;

public class Geometries extends Intersectable {
    final private List<Intersectable> geometries = new LinkedList<>();

    public Geometries() {}

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }

    /**
     * Finds the intersection points between a ray and the geometries.
     *
     * @param ray the ray to intersect with
     * @return list of intersection points, or empty list if no intersections
     */
//    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance) {
//        List<GeoPoint> intersectionPoints = null;
//        for (Intersectable geometry : geometries) {
//            List<GeoPoint> geometryPoints = geometry.findGeoIntersections(ray,distance);
//            if (geometryPoints != null) {
//                if (intersectionPoints == null) {
//                    intersectionPoints = new LinkedList<>();
//                }
//                intersectionPoints.addAll(geometryPoints);
//            }
//        }
//        if(intersectionPoints!=null)
//            return intersectionPoints
//                    .stream()
//                    .sorted(Comparator.comparingDouble(p->((GeoPoint)p).point.distance(ray.getHead())))
//                    .toList();
//        return null;
//    }
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance){
        LinkedList<GeoPoint> points = null;
        for (var geometry : geometries) {
            var geometryList = geometry.findGeoIntersectionsHelper(ray,distance);
            if (geometryList != null) {
                if (points == null) {
                    points = new LinkedList<>();
                }
                points.addAll(geometryList);
            }
        }
        return points;
    }


}
