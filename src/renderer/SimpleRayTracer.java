package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import geometries.Geometry;

import java.util.List;

import static java.lang.Math.*;
import static primitives.Util.alignZero;

/**
 * Implementation of a simple ray tracer using the Phong reflectance model for local effects.
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double MIN_CALC_COLOR_K = 0.001;

    /**
     * Constructor defined by scene
     *
     * @param scene - the scene to be traced
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }


    /**
     * Traces a ray into the scene and calculates the resulting color.
     *
     * @param ray the ray to trace
     * @return the color observed along the ray's path
     */

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = scene._geometries.findGeoIntersectionsHelper(ray, Double.POSITIVE_INFINITY);

        if (intersections == null)
            return scene._background;

        return calcColor(ray.findClosestGeoPoint(intersections), ray);
    }


    /**
     * Calculates the color at a given intersection point using Phong reflectance model.
     *
     * @param geoPoint the intersection point
     * @param ray      the ray intersecting the point
     * @return the calculated color at the intersection point
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        if (geoPoint == null)
            return scene._background;
        return scene._ambientLight.getIntensity().add(calcLocalEffects(geoPoint, ray));

    }

    /**
     * Calculates the local effects (diffuse and specular reflections) at an intersection point.
     *
     * @param geoPoint the intersection point
     * @param ray      the ray intersecting the point
     * @return the color due to local effects at the intersection point
     */
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

        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(point);
            double nl = alignZero(n.dotProduct(l));
            if ((nl * nv > 0) && unshaded(geoPoint, lightSource, l, n, nl)) {
                Color iL = lightSource.getIntensity(point);
                color = color.add(
                        iL.scale(calcDiffusive(material, nl)
                                .add(calcSpecular(material, n, l, nl, v))));
            }
        }
        return color;
    }


    /**
     * Calculates the diffuse reflection contribution based on the material and light interaction.
     *
     * @param material the material of the intersected geometry
     * @param nl       the dot product of normal and light vector
     * @return the diffuse reflection contribution as a {@link Double3} vector
     */
    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(abs(nl));
    }

    /**
     * Calculates the specular reflection contribution based on the material and light interaction.
     *
     * @param material the material of the intersected geometry
     * @param n        the normal vector at the intersection point
     * @param l        the light direction vector
     * @param nl       the dot product of normal and light vector
     * @param v        the view direction vector
     * @return the specular reflection contribution as a {@link Double3} vector
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        // Reflectance vector
        Vector r = l.subtract(n.scale(nl * 2));
        // Max between 0 and -v*r
        double max = max(0, v.scale(-1).dotProduct(r));

        return material.kS.scale(pow(max, material.nShininess));
    }

    private boolean unshaded(GeoPoint geoPoint, LightSource light, Vector l, Vector n, double ln) {
        Vector lightDirection = l.scale(-1);
        Ray ray = new Ray(geoPoint.point, lightDirection);
        List<GeoPoint> intersections = scene._geometries.findGeoIntersections(ray);
        if (intersections == null) return true;
        else {
            double lightDistance = light.getDistance(geoPoint.point);
            for (GeoPoint gp : intersections) {
                if (alignZero(gp.point.distance(geoPoint.point) - lightDistance) <= 0 && gp.geometry.getMaterial().kT == Double3.ZERO) {
                    return false;
                }
            }
        }
        return true;
    }

    private GeoPoint findClosestIntersection(Ray reflectedRay) {
        List<GeoPoint> intersections = scene._geometries.findGeoIntersectionsHelper(reflectedRay, Double.POSITIVE_INFINITY);
        if (intersections == null) {
            return null;
        }
        return reflectedRay.findClosestGeoPoint(intersections);
    }
}/**
    private Color calcSecondaryRayColor(Ray ray, int level, Double3 k) {
        GeoPoint geoPoint = findClosestIntersection(ray);
        Color color = scene._background;
        if (geoPoint != null) {
            color = calcColor(geoPoint, ray, level - 1, k);
        }
        return color;
    }

    private Ray constructRefractedRay(Point point, Ray ray, Vector v) {
        return new Ray(point, ray.getDirection(), v);
    }
    private Color calcGlobalEffects(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Vector n = intersection.geometry.getNormal(intersection.point);
        Ray reflectedRay = constructRefractedRay(intersection.point, ray, n);
        // GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);

        Double3 kkr = intersection.geometry.getMaterial().kR.product(k);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) { // check if the color it would add is Negligible
            color = color.add(calcSecondaryRayColor(reflectedRay, level, kkr)
                    .scale(intersection.geometry.getMaterial().kR));
        }

        Double3 kkt = intersection.geometry.getMaterial().kT.product(k);
        Ray refractedRay = constructRefractedRay(intersection.point, ray, n);
        // GeoPoint refractedPoint = findClosestIntersection(refractedRay);
        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) { // check if the color it would add is Negligible
            color = color.add(calcSecondaryRayColor(refractedRay, level, kkt)
                    .scale(intersection.geometry.getMaterial().kT));
        }

        return color;  private Color calcGlobalEffects(GeoPoint intersection, Ray ray, int level, Double3 k) {
            Color color = Color.BLACK;
            Vector n = intersection.geometry.getNormal(intersection.point);
            Ray reflectedRay = constructReflectedRay(intersection.point, ray, n);
            // GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);

            Double3 kkr = intersection.geometry.getMaterial().kR.product(k);
            if (!kkr.lowerThan(MIN_CALC_COLOR_K)) { // check if the color it would add is Negligible
                color = color.add(calcSecondaryRayColor(reflectedRay, level, kkr)
                        .scale(intersection.geometry.getMaterial().kR));
            }

            Double3 kkt = intersection.geometry.getMaterial().kT.product(k);
            Ray refractedRay = constructRefractedRay(intersection.point, ray, n);
            // GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (!kkt.lowerThan(MIN_CALC_COLOR_K)) { // check if the color it would add is Negligible
                color = color.add(calcSecondaryRayColor(refractedRay, level, kkt)
                        .scale(intersection.geometry.getMaterial().kT));
            }

            return color;
        }
    }
}**/




