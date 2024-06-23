package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

public abstract class RayTracerBase {
    protected scene.Scene scene;

    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    public abstract Color traceRay(Ray ray);

}
