package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;
import geometries.Geometry;
import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import java.util.List;

import static java.lang.Math.*;
import static primitives.Util.alignZero;

/**
 * Implementation of RayTracerBase, basic ray tracing
 */
public class SimpleRayTracer extends RayTracerBase {
    private static final double DELTA = 0.1;
    private boolean unshaded(GeoPoint geoPoint, LightSource light, Vector l, Vector n,double ln) {
        Vector lightDirection = l.scale(-1);
        Ray ray = new Ray(geoPoint.point, lightDirection);
        List<GeoPoint> intersections = scene._geometries.findGeoIntersections(ray);

        if (intersections == null)
            return true;

        intersections.removeIf(
                (item) -> {
                    return item.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K);
                }
        );
        if (intersections.isEmpty())
            return true;
        else
            return false;
    //return intersections == null || intersections.isEmpty();

    }




    /**
     * Constructor defined by scene
     * @param scene - the scene to be traced
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = scene._geometries.findGeoIntersectionsHelper(ray,Double.POSITIVE_INFINITY);

        if (intersections == null)
            return scene._background;

        return calcColor(ray.findClosestGeoPoint(intersections),ray);
    }

    /**
     *  Calculate local effects using Phong Reflectance Model */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene._ambientLight.getIntensity().add(calcLocalEffects(geoPoint,ray));
    }

    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray) {
        Geometry geometry = geoPoint.geometry;
        Point point = geoPoint.point;

        Vector n = geometry.getNormal(point);
        Vector v = ray.getDirection();
        double nv = alignZero(n.dotProduct(v));

        if (nv == 0)
            return Color.BLACK;

        Material material = geometry.getMaterial();
        Color color = geometry.getEmission();

        for (LightSource lightSource : scene.lights){
            Vector l = lightSource.getL(point);
            double nl = alignZero(n.dotProduct(l));
            if ( (nl * nv > 0)  && unshaded(geoPoint, lightSource, l, n, nl)){
                Color iL = lightSource.getIntensity(point);
                color = color.add(
                        iL.scale(calcDiffusive(material,nl)
                                .add(calcSpecular(material, n, l, nl, v))));
            }
        }
        return color;
    }

    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(abs(nl));
    }

    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        // Reflectance vector
        Vector r = l.subtract(n.scale(nl * 2));
        // Max between 0 and -v*r
        double max = max(0,v.scale(-1).dotProduct(r));

        return material.kS.scale(pow(max, material.nShininess));
    }

}




