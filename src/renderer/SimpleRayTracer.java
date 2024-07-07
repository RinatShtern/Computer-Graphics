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
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
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
        Color result = scene._background;
        List<GeoPoint> allPoints = scene._geometries.findGeoIntersectionsHelper(ray, Double.POSITIVE_INFINITY);
        if (allPoints != null) {
            GeoPoint pt = ray.findClosestGeoPoint(allPoints);
            result = calcColor(pt, ray);
        }
        return result;
    }

    /**
     * Calculates the color at a given intersection point using the Phong reflectance model.
     *
     * @param geoPoint the intersection point
     * @param ray      the ray intersecting the point
     * @return the calculated color at the intersection point
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
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

        if (nv == 0) {
            return Color.BLACK;
        }

        Material material = geometry.getMaterial();
        Color color = geometry.getEmission();

        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {  // sign(nl) == sign(nv)
                if (unshaded(geoPoint, lightSource, l, n, nl)) {
                    Color iL = lightSource.getIntensity(geoPoint.point);
                    color = color.add(
                            iL.scale(calcDiffusive(material, nl)),
                            iL.scale(calcSpecular(material, n, l, nl, v))
                    );
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
