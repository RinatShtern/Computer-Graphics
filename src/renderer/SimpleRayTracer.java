package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

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

    /**
     * Traces the given ray and calculates the color at the closest intersection point.
     * If there are no intersections, returns the background color of the scene.
     *
     * @param ray the ray to be traced
     * @return the color at the closest intersection point or the background color if no intersections are found
     */
    public Color traceRay(Ray ray) {
        List<Point> intersections = this.scene.getGeometries().findIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return this.scene.getBackground();
        }
        Point closestPoint = ray.findClosestPoint(intersections);
        return calcColor(closestPoint);
    }

    /**
     * Calculates the color at the given point.
     * Currently, it returns the ambient light intensity of the scene.
     *
     * @param point the point at which the color is to be calculated
     * @return the color at the given point
     */
    private Color calcColor(Point point) {
        return scene.getAmbientLight().getIntensity();
    }
}
