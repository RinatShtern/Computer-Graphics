package lighting;

import primitives.*;

/**
 * Represents a spotlight light source in a scene.
 * A spotlight emits light in a specific direction from a single point, with an additional angular attenuation factor.
 */
public class SpotLight extends PointLight {

    private Vector direction; // The direction of the spotlight

    /**
     * Constructs a spotlight with the given intensity, position, and direction.
     *
     * @param intensity  the intensity of the light
     * @param position   the position of the light
     * @param direction  the direction in which the spotlight emits light
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Constructs a spotlight with the given intensity, position, direction, and radius.
     *
     * @param intensity  the intensity of the light
     * @param position   the position of the light
     * @param direction  the direction in which the spotlight emits light
     * @param radius     the radius of the light source
     */
    public SpotLight(Color intensity, Point position, Vector direction, double radius) {
        super(intensity, position, radius);
        this.direction = direction.normalize();
    }

    /**
     * Sets the constant attenuation factor for the spotlight.
     *
     * @param kC the constant attenuation factor
     * @return the spotlight itself (for chaining)
     */
    @Override
    public SpotLight setkC(double kC) {
        super.setkC(kC);
        return this;
    }

    /**
     * Sets the linear attenuation factor for the spotlight.
     *
     * @param kL the linear attenuation factor
     * @return the spotlight itself (for chaining)
     */
    @Override
    public SpotLight setkL(double kL) {
        super.setkL(kL);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor for the spotlight.
     *
     * @param kQ the quadratic attenuation factor
     * @return the spotlight itself (for chaining)
     */
    @Override
    public SpotLight setkQ(double kQ) {
        super.setkQ(kQ);
        return this;
    }

    /**
     * Gets the intensity of the light at a given point, considering the spotlight's direction.
     *
     * @param p the point at which the intensity is calculated
     * @return the intensity of the light at the given point, adjusted by the spotlight's direction
     */
    @Override
    public Color getIntensity(Point p) {
        return super.getIntensity(p).scale(Double.max(0, direction.dotProduct(getL(p))));
    }

    /**
     * Gets the direction vector to the light source from a given point.
     *
     * @param p the point from which the direction is calculated
     * @return the direction vector to the light source
     */
    @Override
    public Vector getL(Point p) {
        return super.getL(p);
    }
}