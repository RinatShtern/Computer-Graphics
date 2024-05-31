package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{
    private List<Intersectable> intersectables;

    public Geometries(){}

    public Geometries(Intersectable... geometries) {
        this.intersectables = new LinkedList<>();
        add(geometries);
    }

    public void add(Intersectable... geometries) {
        this.intersectables.addAll(Arrays.asList(geometries));
    }

    @Override
    public List<Point> findIntsersections(Ray ray) {
        return List.of();
    }

    public CharSequence getIntersectables() {
        return this.intersectables.toString();
    }

}