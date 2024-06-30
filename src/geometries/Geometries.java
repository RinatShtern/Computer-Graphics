package geometries;

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
