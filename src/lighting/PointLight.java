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
}
