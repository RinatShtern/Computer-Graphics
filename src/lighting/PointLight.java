package lighting;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represents a point light source in a scene.
 * A point light source emits light equally in all directions from a single point.
 */
public class PointLight extends Light implements LightSource {

    protected Point position; // The position of the point light
    private double kC = 1d; // Constant attenuation factor
    private double kL = 0d; // Linear attenuation factor
    private double kQ = 0d; // Quadratic attenuation factor
    private double radius = 0d;


    /**
     * Constructs a point light with the given intensity and position.
     *
     * @param intensity the intensity of the light
     * @param position the position of the light
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    public PointLight(Color intensity, Point position, double radius) {
        super(intensity);
        this.position = position;
        this.radius = radius;
    }

    /**
     * Sets the constant attenuation factor.
     *
     * @param kC the constant attenuation factor
     * @return the point light itself (for chaining)
     */
    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor.
     *
     * @param kL the linear attenuation factor
     * @return the point light itself (for chaining)
     */
    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor.
     *
     * @param kQ the quadratic attenuation factor
     * @return the point light itself (for chaining)
     */
    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Gets the intensity of the light at a given point.
     *
     * @param p the point at which the intensity is calculated
     * @return the intensity of the light at the given point
     */
    @Override
    public Color getIntensity(Point p) {
        double disSquared = position.distanceSquared(p);
        double dis = position.distance(p);
        return getIntensity().scale(1 / (kC + kL * dis + kQ * disSquared));
    }

    /**
     * Gets the direction vector to the light source from a given point.
     *
     * @param p the point from which the direction is calculated
     * @return the direction vector to the light source, or null if the point is at the light source position
     */
    @Override
    public Vector getL(Point p) {
        if (p.equals(position)) {
            return null;
        }
        return p.subtract(position).normalize();
    }

    /**
     * Gets the distance from a given point to the light source.
     *
     * @param p the point from which the distance is calculated
     * @return the distance to the light source
     */
    @Override
    public double getDistance(Point p) {
        return position.distance(p);
    }


    @Override
    public List<Vector> getRayBeam(Point p){
        Vector vec2light = getL(p);
        if (this.radius == 0)
            return List.of(vec2light);

        Vector vec1 = vec2light.makePerpendicularVector();
        Vector vec2 = vec2light.crossProduct(vec1);

        List<Vector> vecs2light = new LinkedList<>();

        for (double i = -10 * radius; i < 10 * radius; i += (10 * radius)/2d){
            for (double j = -10 * radius; j < 10 * radius; j += (10 * radius)/2d){
                try {
                    vecs2light.add(p.subtract(position.add(vec1.scale(i)).add(vec2.scale(j))));
                }
                catch (IllegalArgumentException e) {
                    vecs2light.add(vec2light);
                }
            }
        }
        return vecs2light;
    }
//@Override
//public List<Vector> getRayBeam(Point p) {
//    Vector vec2light = getL(p);
//    if (this.radius == 0) {
//        return List.of(vec2light);
//    }
//
//    Vector vec1 = vec2light.makePerpendicularVector();
//    Vector vec2 = vec2light.crossProduct(vec1);
//
//    List<Vector> vecs2light = new LinkedList<>();
//    double step = radius / 5.0; // Adjust step to control the density of rays
//
//    for (double i = -radius; i <= radius; i += step) {
//        for (double j = -radius; j <= radius; j += step) {
//            try {
//                Point perturbedPoint = position.add(vec1.scale(i)).add(vec2.scale(j));
//                Vector perturbedVector = p.subtract(perturbedPoint).normalize();
//                vecs2light.add(perturbedVector);
//            } catch (IllegalArgumentException e) {
//                vecs2light.add(vec2light);
//            }
//        }
//    }
//    return vecs2light;
}

