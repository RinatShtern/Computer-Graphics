package geometries;

import primitives.*;
import primitives.Point;

import java.util.List;


public interface Intersectable {
    List<Point> findIntsersections(Ray ray);

}
