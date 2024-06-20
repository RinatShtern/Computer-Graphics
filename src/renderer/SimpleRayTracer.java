package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

public class SimpleRayTracer {

    protected  Scene scene;

    public SimpleRayTracer(Scene scene) {
    }

    public void RayTracerBase(Scene scene){
        this.scene = scene;
    }

    public abstract Color traceRay(Ray ray);
}
