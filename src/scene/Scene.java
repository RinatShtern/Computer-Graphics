package scene;

import geometries.Geometries;
import geometries.Intersectable;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {
    private  String _name;
    private Color _background;
    private  AmbientLight _ambientLight = AmbientLight.NONE;
    private  Geometries _geometries = new Geometries();

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
