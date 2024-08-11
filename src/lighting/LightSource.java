package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import java.util.List;

/**
 * Interface for light sources in a scene.
 * Defines the methods that all light sources must implement.
 */
public interface LightSource {

    /**
     * Gets the intensity of the light at a given point.
     *
     * @param p the point at which the intensity is calculated
     * @return the intensity of the light
     */
    public Color getIntensity(Point p);

    /**
     * Gets the direction vector to the light source from a given point.
     *
     * @param p the point from which the direction is calculated
     * @return the direction vector to the light source
     */
    public Vector getL(Point p);

    /**
     * Gets the distance from a given point to the light source.
     *
     * @param p the point from which the distance is calculated
     * @return the distance to the light source
     */
    public double getDistance(Point p);

    /**
     * Generates a beam of rays from the light source towards a given point.
     *
     * @param p the point towards which the beam of rays is generated
     * @return a list of direction vectors representing the rays in the beam
     */
    public List<Vector> getRayBeam(Point p);
}