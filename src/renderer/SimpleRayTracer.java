package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase{

    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    public Color traceRay(Ray ray){
        List<Point> intersections =this.scene.getGeometries().findIntersections(ray);
        if(intersections == null || intersections.isEmpty()){
            return this.scene.getBackground();
        }
        Point closestPoint = ray.findClosestPoint(intersections);
        return calcColor(closestPoint);
    }

    private Color calcColor(Point point) {
        return scene.getAmbientLight().getIntensity();
    }

}
