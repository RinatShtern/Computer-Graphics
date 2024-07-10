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
import static primitives.Util.isZero;

/**
 * Implementation of a simple ray tracer using the Phong reflectance model for local effects.
 */
public class SimpleRayTracer extends RayTracerBase {
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * The initial value of k
     */
    private static final double INITIAL_K = 1;

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
        Color result = scene._background;
        List<GeoPoint> allPoints = scene._geometries.findGeoIntersectionsHelper(ray, Double.POSITIVE_INFINITY);
        if (allPoints != null) {
            GeoPoint pt = ray.findClosestGeoPoint(allPoints);
            result = calcColor(pt, ray);
        }
        return result;
    }

    /**
     * Calculate the color of the intersection between the ray at the given point on a geometry
     *
     * @param intersection a given point and geometry
     * @param ray          a given ray
     * @return color at point
     */
    private Color calcColor(GeoPoint intersection, Ray ray) {
//        return scene.ambientLight.getIntensity().add(calcLocalEffects(intersection, ray));
        return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, new Double3(INITIAL_K))
                .add(scene._ambientLight.getIntensity());
    }

    /**
     * Calculate the color at a point
     *
     * @param intersection the point
     * @param ray          the ray that hit the point
     * @param level        the level of the recursion
     * @param k            the k value of the point
     * @return the color at the point
     */
    private Color calcColor(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(intersection, ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray, level, k));
    }
    /**
     * Construct the reflected ray
     *
     * @param gp        the point to reflect
     * @param direction the direction of the ray
     * @param n         the normal at the point
     * @return the reflected ray
     */
    private Ray constructReflectedRay(GeoPoint gp, Vector direction, Vector n) {
        Vector mirror = direction.subtract(n.scale(direction.dotProduct(n) * 2));
        return new Ray(gp.point, mirror, n);

    }

    /**
     * Construct the refracted ray
     *
     * @param gp        the point to refract
     * @param direction the direction of the ray
     * @param normal    the normal at the point
     * @return the refracted ray
     */
    private Ray constructRefractedRay(GeoPoint gp, Vector direction, Vector normal) {
        return new Ray(gp.point, direction, normal);
    }

    /**
     * Calculate the global effects at a point
     *
     * @param gp    the point
     * @param ray   the ray
     * @param level the level of the recursion
     * @param k     the k value of the point
     * @return the color at the point
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Material material = gp.geometry.getMaterial();
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);

        return calcGlobalEffect(constructReflectedRay(gp, v, n), level, k, material.kR)
                .add(calcGlobalEffect(constructRefractedRay(gp, v, n), level, k, material.kT));
    }
    /**
     * Find the closest intersection of a ray with the scene
     *
     * @param ray the ray
     * @return the closest intersection
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        return ray.findClosestGeoPoint(scene._geometries.findGeoIntersections(ray));
    }

    /**
     * Calculate the transparency of the point
     *
     * @param gp    the point
     * @param l     the light vector
     * @param n     the normal at the point
     * @param light the light source
     * @return the transparency of the point
     */
    private Double3 transparency(GeoPoint gp, Vector l, Vector n, LightSource light) {
        Ray lightRay = new Ray(gp.point, l.scale(-1), n); // from point to light source
        var intersections = scene._geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));
        Double3 ktr = Double3.ONE;

        if (intersections == null)
            return ktr;

        for (GeoPoint p : intersections) {
            ktr = ktr.product(p.geometry.getMaterial().kT);
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                return Double3.ZERO;
        }

        return ktr;
    }

    /**
     * Calculate the global effects at a point
     *
     * @param ray   the ray to calculate the color for
     * @param level the level of the recursion
     * @param k     the color at the point
     * @param kx    the color of the effect
     * @return the color at the point
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        if (gp == null)
            return scene._background.scale(kx);
        return isZero(gp.geometry.getNormal(gp.point).dotProduct(ray.getDirection())) ? Color.BLACK
                : calcColor(gp, ray, level - 1, kkx).scale(kx);
    }

    /**
     * calculate the color between the ray and the point
     *
     * @param gp  point and geometry
     * @param ray a given ray
     * @return the color at the point
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Color color = gp.geometry.getEmission();

        Vector n = gp.geometry.getNormal(gp.point); // Normal to point
        Vector v = ray.getDirection(); // Ray's direction
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return color;

        Material material = gp.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));

            if (nl * nv > 0) {
                Double3 ktr = transparency(gp, l, n, lightSource);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr);
                    color = color.add(iL.scale(calcDiffusive(material, nl).add(calcSpecular(material, n, l, nl, v))));
                }
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

    /**
     * Determines if a point is unshaded by other objects for a given light source.
     *
     * @param geoPoint  the intersection point
     * @param light     the light source
     * @param l         the light direction vector
     * @param n         the normal vector at the intersection point
     * @param nl        the dot product of normal and light vector
     * @return true if the point is unshaded, false otherwise
     */
    private boolean unshaded(GeoPoint geoPoint, LightSource light, Vector l, Vector n, double nl) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector; // if needed, changes the vector's direction
        if (nl > 0) {
            epsVector = n.scale(-DELTA);
        } else {
            epsVector = n.scale(DELTA);
        }

        Point point = geoPoint.point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        double distance = light.getDistance(point); // distance from light to geo point
        List<GeoPoint> intersections = scene._geometries.findGeoIntersections(lightRay, distance);

        if (intersections == null) {
            return true;
        }
        for (GeoPoint gp : intersections) {
            if (point.distance(gp.point) < distance) {
                return false;
            }
        }
        return true;
    }
}
