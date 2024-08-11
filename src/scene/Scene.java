package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a scene in a 3D space.
 * A scene contains various objects, lights, and background information that can be rendered.
 */
public class Scene {
    public String _name; // The name of the scene
    public Color _background = Color.BLACK; // The background color of the scene
    public AmbientLight _ambientLight = AmbientLight.NONE; // The ambient light of the scene
    public Geometries _geometries = new Geometries(); // The geometries in the scene
    public List<LightSource> lights = new LinkedList<>(); // The list of light sources in the scene

    /**
     * Constructs a new Scene with the given name.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        _name = name;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the background color
     * @return the scene itself (for chaining)
     */
    public Scene setBackground(Color background) {
        this._background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the ambient light
     * @return the scene itself (for chaining)
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this._ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries in the scene.
     *
     * @param geometries the geometries
     * @return the scene itself (for chaining)
     */
    public Scene setGeometries(Geometries geometries) {
        this._geometries = geometries;
        return this;
    }

    /**
     * Sets the list of light sources in the scene.
     *
     * @param lights the light sources
     * @return the scene itself (for chaining)
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    /**
     * Gets the name of the scene.
     *
     * @return the name of the scene
     */
    public String getName() {
        return _name;
    }

    /**
     * Gets the background color of the scene.
     *
     * @return the background color
     */
    public Color getBackground() {
        return _background;
    }

    /**
     * Gets the ambient light of the scene.
     *
     * @return the ambient light
     */
    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    /**
     * Gets the geometries in the scene.
     *
     * @return the geometries
     */
    public Geometries getGeometries() {
        return _geometries;
    }
}