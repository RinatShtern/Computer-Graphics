package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase{

    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    public Color traceRay(Ray ray){
        List<GeoPoint> intersections =this.scene._geometries.findGeoIntersections(ray);

        if(intersections == null || intersections.isEmpty()){
            return this.scene.getBackground();
        }

        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
        return calcColor(closestPoint);
    }

    private Color calcColor(GeoPoint geoPoint) {

        Color result = this.scene._ambientLight.getIntensity();
        return result.add(geoPoint.geometry.getEmission());

    }

}
