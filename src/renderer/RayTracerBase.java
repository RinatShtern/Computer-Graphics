package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

/**
 * Abstract base class for ray tracers.
 * Provides the basic structure for tracing rays in a scene and calculating the resulting color.
 */
public abstract class RayTracerBase {
    /**
     * The scene to be used for ray tracing.
     */
    protected Scene scene;

    /**
     * Constructs a RayTracerBase with the given scene.
     *
     * @param scene the scene to be used for ray tracing
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Abstract method for tracing a ray and calculating the color at the intersection points.
     * Must be implemented by subclasses.
     *
     * @param ray the ray to be traced
     * @return the color at the intersection point or points
     */
    public abstract Color traceRay(Ray ray);
}
