package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a directional light source that emits light in a specific direction.
 */
public class DirectionalLight extends Light implements LightSource {
    private final Vector direction;

    /**
     * Constructs a directional light with the given intensity and direction.
     *
     * @param intensity the intensity of the light
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * Gets the intensity of the light at a given point.
     *
     * @param p the point at which the intensity is calculated
     * @return the intensity of the light
     */
    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    /**
     * Gets the direction vector to the light source from a given point.
     *
     * @param p the point from which the direction is calculated
     * @return the direction vector to the light source
     */
    @Override
    public Vector getL(Point p) {
        return this.direction;
    }

    /**
     * Gets the distance from a given point to the light source.
     *
     * @param p the point from which the distance is calculated
     * @return the distance to the light source (always positive infinity for directional light)
     */
    @Override
    public double getDistance(Point p) {
        return Double.POSITIVE_INFINITY;
    }
}
