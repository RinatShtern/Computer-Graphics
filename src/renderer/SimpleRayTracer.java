package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

/**
 * The SimpleRayTracer class is responsible for tracing rays and calculating the color
 * at the intersection points in the scene.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a SimpleRayTracer with the given scene.
     *
     * @param scene the scene to be used for ray tracing
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    public Color traceRay(Ray ray){
        List<GeoPoint> intersections =this.scene._geometries.findGeoIntersectionsHelper(ray,Double.POSITIVE_INFINITY);

        if(intersections == null || intersections.isEmpty()){
            return scene._background;
        }

        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
        return calcColor(closestPoint);

    }

    private Color calcColor(GeoPoint geoPoint) {

       // Color result = this.scene._ambientLight.getIntensity();
        //return result.add(geoPoint.geometry.getEmission());
        return scene._ambientLight.getIntensity().add(geoPoint.geometry.getEmission());
    }

}






