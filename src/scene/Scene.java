package scene;

import geometries.Geometries;
import geometries.Intersectable;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

public class Scene {
    public   String _name;
    public Color _background = Color.BLACK;
    public  AmbientLight _ambientLight = AmbientLight.NONE;
    public  Geometries _geometries = new Geometries();
    public List<LightSource> lights = new LinkedList<>();

    public Scene(String name){
        _name = name;
    }

    public Scene setBackground(Color background) {
        this._background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this._ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        this._geometries = geometries;
        return this;
    }
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    public String getName() {
        return _name;
    }

    public Color getBackground() {
        return _background;
    }

    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    public Geometries getGeometries() {
        return _geometries;
    }

}
