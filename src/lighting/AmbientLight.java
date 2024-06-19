package lighting;
import primitives.Color;
import primitives.Double3;
/**
 * class AmbientLight extends of light to show the ambient light of the scene
 * the light have color
 */
public class AmbientLight {

    private final Color intensity;  // light intensity as a Color
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, 0.0);
    /**
     * primary constructor
     * @param Ia basic illumination
     * @param Ka attenuation factor
     */
    public AmbientLight(Color Ia , Double3 Ka){
        intensity = Ia.scale(Ka);
    }

    public AmbientLight(Color Ia , double Ka){
        intensity = Ia.scale(Ka);
    }

    /**
     * getter for intensity
     * @return the actual intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
