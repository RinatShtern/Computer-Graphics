package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public abstract class RayTracerBase extends SimpleRayTracer{
    protected RayTracerBase(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        Color result = scene.getBackground();
        List<Point> allPoints = scene.getGeometries().findIntersections(ray);
        if(allPoints != null){
            Point pt = ray.findClosestPoint(allPoints);
            result = calcColor(pt);
        }
        return result;
    }

    private Color calcColor(Point point) {
        return scene.getAmbientLight().getIntensity();
    }
}
