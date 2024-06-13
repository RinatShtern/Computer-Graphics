package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

public class Camera implements Cloneable {

    private Point Plocation ;
    private Vector right ;
    private Vector up ;
    private Vector to ;
    double _height = 0;
    double _width = 0;
    double distance = 0;

    public Point getLocation() {

        return Plocation;
    }

    public Vector getRight() {
        return right;
    }

    public Vector getUp() {
        return up;
    }

    public Vector getTo() {
        return to;
    }

    public double getHeight() {
        return _height;
    }

    public double getWidth() {
        return _width;
    }

    public double getDistance() {
        return distance;
    }

    public Camera setVPDistance(double distance) {
        this.distance = distance;
        return this;
    }
    public Camera setVPSize(double width, double height) {
        _width = width;
        _height = height;
        return this;
    }


    public Camera(Point location, Vector up, Vector to) {
        this.Plocation = location;

        if (!isZero(up.dotProduct(to))) {
            throw new IllegalArgumentException("vup and vto are not orthogonal");
        }
        this.up = up.normalize();
        this.to = to.normalize();
        this.right = to.crossProduct(up);
    }

    private Camera() {
    }

    //nX-sum of columns
    //nY-sum of lines
    //j-column in view plane
    //i-line in view plane
    public Ray constructRay(int nX, int nY, int j, int i) {
        double Rx = _width / nX;
        double Ry = _height / nY;

        Point pIJ = Plocation.add(to.scale(distance));

        double xJ = (j - (nX - 1) / 2d) * Rx;
        double yI = -(i - (nY - 1) / 2d) * Ry;

        if (isZero(xJ) && isZero(yI)) {
            return new Ray(Plocation, pIJ.subtract(Plocation));
        } else {
            if (!isZero(xJ))
                pIJ = pIJ.add(right.scale(xJ));
            if (!isZero(yI))
                pIJ = pIJ.add(up.scale(yI));

        }
        return new Ray(Plocation, pIJ.subtract(Plocation));
    }



    public static Builder getBuilder() {
        return new Builder();
    }



    public static class Builder {
        final private Camera camera = new Camera();

        public Builder() {
        }

        public Builder setLocation(Point point_location) {
            if (point_location == null) {
                throw new IllegalArgumentException("Location cannot be null");
            }
            camera.Plocation = point_location;
            return this;
        }

        public Builder setDirection(Vector to, Vector up) {
            if (to == null || up == null) {
                throw new IllegalArgumentException("Vectors cannot be null");
            }
            camera.to = to.normalize();
            camera.right = camera.to.crossProduct(up).normalize();
            camera.up = camera.right.crossProduct(camera.to).normalize();
            return this;
        }

        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            camera._width = width;
            camera._height = height;
            return this;
        }

        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            camera.distance = distance;
            return this;
        }

        public Camera build() throws CloneNotSupportedException {
            if (camera.Plocation == null) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "location");
            }
            if (camera.to == null || camera.up == null || camera.right == null) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "direction vectors");
            }
            if (camera._width == 0.0) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "view plane width");
            }
            if (camera._height == 0.0) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "view plane height");
            }
            if (camera.distance == 0.0) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "view plane distance");
            }
            return (Camera) camera.clone();
        }
    }

}