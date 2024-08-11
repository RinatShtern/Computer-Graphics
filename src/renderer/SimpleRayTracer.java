package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import geometries.Geometry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.awt.Color.BLACK;
import static java.lang.Math.*;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Small value used to shift the head point of the ray to avoid self-shadowing.
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;

    /**
     * The maximum recursion level for calculating color.
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /**
     * The minimum value for the attenuation factor to continue recursion.
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * The initial value of k
     */
    private static final double INITIAL_K = 1;

    private boolean softShadows = true;

    /**
     * Constructor
     *
     * @param scene A scene where the department is initialized
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Sets the soft shadows flag.
     *
     * @param enable true to enable soft shadows, false to disable
     * @return the current instance of SimpleRayTracer
     */
    public SimpleRayTracer setSoftShadows(boolean enable) {
        softShadows = enable;
        return this;
    }

    /**
     * Traces a ray into the scene and calculates the resulting color.
     *
     * @param ray the ray to trace
     * @return the color observed along the ray's path
     */
    @Override
    public Color traceRay(Ray ray) {
        var intersection = findClosestIntersection(ray);
        return intersection == null ? scene._background : calcColor(intersection, ray);
    }

    /**
     * Traces the rays and calculates the color of the point that interacts with the geometries of the scene.
     *
     * @param rays the rays that came out of the camera
     * @return the color of the object that the rays interact with
     */
    @Override
    public Color TraceRays(List<Ray> rays) {
        Color color = new Color(BLACK);
        for (Ray ray : rays) {
            GeoPoint closestGeoPoint = findClosestIntersection(ray);
            if (closestGeoPoint == null)
                color = color.add(scene._background);
            else color = color.add(calcColor(closestGeoPoint, ray));
        }
        return color.reduce(rays.size());
    }

    /**
     * Calculates the color of the intersection between the ray at the given point on a geometry.
     *
     * @param intersection a given point and geometry
     * @param ray          a given ray
     * @return color at point
     */
    private Color calcColor(GeoPoint intersection, Ray ray) {
        return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, new Double3(INITIAL_K))
                .add(scene._ambientLight.getIntensity());
    }

    /**
     * Calculate the color at a point.
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
     * Constructs the reflected ray.
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
     * Constructs the refracted ray.
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
     * Finds the closest intersection of a ray with the scene.
     *
     * @param ray the ray
     * @return the closest intersection
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        return ray.findClosestGeoPoint(scene._geometries.findGeoIntersections(ray));
    }

    /**
     * Calculates the global effects at a point.
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
     * Calculates the global effects at a point.
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
     * Calculates the color between the ray and the point.
     *
     * @param gp  point and geometry
     * @param ray a given ray
     * @return the color at the point
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray, Double3 k) {
        Color color = gp.geometry.getEmission(); // Get the emission color of the geometry

        Vector n = gp.geometry.getNormal(gp.point); // Calculate the normal to the point on the object
        Vector v = ray.getDirection(); // Direction of the ray
        double nv = alignZero(n.dotProduct(v)); // Calculate the dot product between the normal and the ray direction
        if (nv == 0) return color; // If the normal and the direction are orthogonal, no lighting is calculated

        Material material = gp.geometry.getMaterial(); // Get the material of the object
        for (LightSource lightSource : scene.lights) { // Loop through all the light sources in the scene
            Vector l = lightSource.getL(gp.point); // Light direction from the point to the light source
            double nl = alignZero(n.dotProduct(l)); // Dot product between the normal and the light direction

            if (nl * nv > 0) { // Check if the light source is illuminating the point and not from the opposite side
                Double3 ktr = softShadows ? calcSoftShadows(gp, n, lightSource) : transparency(gp, l, n, lightSource);
                // If soft shadows are enabled, calculate transparency using calcSoftShadows, otherwise calculate regular transparency

                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) { // If the transparency is sufficient to contribute to the final color
                    Color iL = lightSource.getIntensity(gp.point).scale(ktr); // Intensity of the light at the point
                    color = color.add(iL.scale(calcDiffusive(material, nl).add(calcSpecular(material, n, l, nl, v))));
                    // Calculate the local color by adding the diffuse reflection and specular reflection
                }
            }
        }
        return color; // Return the calculated color
    }

    /**
     * Calculates the transparency of the point.
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
     * Calculates soft shadows using a beam of light vectors.
     *
     * @param gp    the point
     * @param n     the normal at the point
     * @param light the light source
     * @return the calculated transparency factor for soft shadows
     */
    private Double3 calcSoftShadows(GeoPoint gp, Vector n, LightSource light) {
        // Create a ray beam of vectors from the light source to the point on the object
        List<Vector> vecs2light = light.getRayBeam(gp.point);

        // Variable to store the sum of transparency for each ray relative to the light source
        Double3 sumKtr = Double3.ZERO;

        // Loop through each vector in the beam
        for (Vector l : vecs2light) {
            // Create a light ray from the geometry point towards the light source, opposite to the direction of vector l
            Ray lightRay = new Ray(gp.point, l.scale(-1), n); // from the point to the light source

            // Calculate the intersections between the light ray and the objects in the scene
            var intersections = scene._geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));

            // Start with the assumption that the ray passes fully (full transparency)
            Double3 ktr = Double3.ONE;

            // If there are no intersections, the ray is unblocked, so full transparency is maintained
            if (intersections == null) {
                sumKtr = sumKtr.add(ktr); // Add the transparency to the sum
                continue; // Continue to the next iteration
            }

            // Loop through each intersection point and check its transparency
            for (GeoPoint p : intersections) {
                ktr = ktr.product(p.geometry.getMaterial().kT); // Calculate cumulative transparency
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) { // If transparency is lower than the minimum value
                    ktr = Double3.ZERO; // The ray is considered fully blocked
                    break; // Exit the loop as there's no need to check further intersections
                }
            }
            sumKtr = sumKtr.add(ktr); // Add the transparency to the overall sum
        }
        // Calculate the average transparency across all the rays in the beam
        return sumKtr.scale(1d / vecs2light.size());
    }

    /**
     * Checks if the point is shaded.
     *
     * @param gp the point and its body
     * @param l vector from the source or to the point
     * @param n the normal to the point
     * @param nl the max distance
     * @param light the light source
     * @return true if the point is unshaded, false if it is shaded
     */
    @SuppressWarnings("unused")
    @Deprecated(forRemoval = true)
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nl, LightSource light) {
        Vector lightDir = l.scale(-1);
        Ray lightRay = new Ray(gp.point.add(n.scale(nl < 0 ? DELTA : -DELTA)), lightDir);

        var intersections = scene._geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));
        // If no intersections, it's unshaded
        if (intersections == null)
            return true;

        // If kT==0 it's shaded
        for (GeoPoint p : intersections)
            if (!Double3.ZERO.equals(p.geometry.getMaterial().kT))
                return false;

        return true;
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
     * Calculates the specular reflection.
     *
     * @param material material of the body
     * @param n        normal to the point on the geometry
     * @param l        vector between the light source and the point
     * @param nl       dot product between the normal and the light vector
     * @param v        ray's direction
     * @return the specular reflection
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        Vector r = l.subtract(n.scale(nl * 2));
        double minusVR = -alignZero(v.dotProduct(r));
        return minusVR > 0 ? material.kS.scale(Math.pow(minusVR, material.nShininess)) : Double3.ZERO;
    }

    /**
     * Performs adaptive super-sampling for a given pixel.
     *
     * @param centerP     The center point of the pixel.
     * @param Width       The width of the pixel.
     * @param Height      The height of the pixel.
     * @param minWidth    The minimum width of a sub-pixel for further sampling.
     * @param minHeight   The minimum height of a sub-pixel for further sampling.
     * @param cameraLoc   The location of the camera.
     * @param Vright      The vector representing the right direction.
     * @param Vup         The vector representing the up direction.
     * @param prePoints   A list of pre-sampled points to avoid redundancy.
     * @return The color computed for the pixel through adaptive super-sampling.
     */
    @Override
    public Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints) {
        if (Width < minWidth * 2 || Height < minHeight * 2) {
            // If the pixel is smaller than the minimum size, trace a ray through the pixel and return the color.
            return this.traceRay(new Ray(cameraLoc, centerP.subtract(cameraLoc)));
        }

        List<Point> nextCenterPList = new LinkedList<>();
        List<Point> cornersList = new LinkedList<>();
        List<primitives.Color> colorList = new LinkedList<>();
        Point tempCorner;
        Ray tempRay;
        // Iterate over the corners of the pixel and perform sub-sampling
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                tempCorner = centerP.add(Vright.scale(i * Width / 2)).add(Vup.scale(j * Height / 2));
                cornersList.add(tempCorner);
                // Check if the sub-pixel's corner is already sampled
                if (prePoints == null || !isInList(prePoints, tempCorner)) {
                    tempRay = new Ray(cameraLoc, tempCorner.subtract(cameraLoc));
                    nextCenterPList.add(centerP.add(Vright.scale(i * Width / 4)).add(Vup.scale(j * Height / 4)));
                    colorList.add(traceRay(tempRay));
                }
            }
        }

        if (nextCenterPList == null || nextCenterPList.size() == 0) {
            // If no valid sub-pixels were found, return black color.
            return primitives.Color.BLACK;
        }

        boolean isAllEquals = true;
        primitives.Color tempColor = colorList.get(0);
        // Check if all colors in the colorList are almost equal
        for (primitives.Color color : colorList) {
            if (!tempColor.isAlmostEquals(color))
                isAllEquals = false;
        }
        if (isAllEquals && colorList.size() > 1)
            // If all colors are equal and there is more than one color, return the first color.
            return tempColor;

        tempColor = primitives.Color.BLACK;
        // Recursively perform adaptive super-sampling on sub-pixels
        for (Point center : nextCenterPList) {
            tempColor = tempColor.add(AdaptiveSuperSamplingRec(center, Width / 2, Height / 2, minWidth, minHeight, cameraLoc, Vright, Vup, cornersList));
        }
        // Reduce the color by dividing by the number of sub-pixels
        return tempColor.reduce(nextCenterPList.size());
    }

    /**
     * Performs regular super-sampling for a given pixel.
     *
     * @param centerP     The center point of the pixel.
     * @param Width       The width of the pixel.
     * @param Height      The height of the pixel.
     * @param minWidth    The minimum width of a sub-pixel for further sampling.
     * @param minHeight   The minimum height of a sub-pixel for further sampling.
     * @param cameraLoc   The location of the camera.
     * @param Right       The vector representing the right direction.
     * @param Vup         The vector representing the up direction.
     * @param prePoints   A list of pre-sampled points to avoid redundancy.
     * @return The color computed for the pixel through regular super-sampling.
     */
    public Color RegularSuperSampling(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Right, Vector Vup, List<Point> prePoints) {
        List<Color> colorList = new ArrayList<>();

        int numSubPixelsX = (int) Math.ceil(Width / minWidth);
        int numSubPixelsY = (int) Math.ceil(Height / minHeight);

        Random random = new Random();
        // Iterate over sub-pixels and perform regular super-sampling
        for (int i = 0; i < numSubPixelsY; i++) {
            for (int j = 0; j < numSubPixelsX; j++) {
                double offsetX = minWidth * j;
                double offsetY = minHeight * i;

                double randomX = offsetX + random.nextDouble() * minWidth;
                double randomY = offsetY + random.nextDouble() * minHeight;

                Point subPixelPoint = centerP.add(Right.scale(randomX - Width / 2)).add(Vup.scale(randomY - Height / 2));

                // Check if the sub-pixel's point is already sampled
                if (prePoints == null || !isInList(prePoints, subPixelPoint)) {
                    Ray ray = new Ray(cameraLoc, subPixelPoint.subtract(cameraLoc));
                    colorList.add(traceRay(ray));
                }
            }
        }

        if (colorList.isEmpty()) {
            // If no valid sub-pixels were found, return black color.
            return primitives.Color.BLACK;
        }

        Color averageColor = Color.BLACK;
        // Calculate the average color by adding all colors in the colorList
        for (Color color : colorList) {
            averageColor = averageColor.add(color);
        }
        // Reduce the color by dividing by the number of sub-pixels
        return averageColor.reduce(colorList.size());
    }

    /**
     * Checks if a point is in the list.
     *
     * @param pointsList the list of points
     * @param point      the point that we look for
     * @return true if the point is in the list, false otherwise
     */
    private boolean isInList(List<Point> pointsList, Point point) {
        for (Point tempPoint : pointsList) {
            if (point.equals(tempPoint))
                return true;
        }
        return false;
    }
}