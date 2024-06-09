package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

public class Camera implements Cloneable {

    private Point location = new Point(0, 0, 0);
    private Vector right = new Vector(0, 0, 0);
    private Vector up = new Vector(0, 0, 0);
    private Vector to = new Vector(0, 0, 0);
    double height = 0;
    double width = 0;
    double distance = 0;

    public Point getLocation() {
        return location;
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

    public double getHight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getDistance() {
        return distance;
    }

    public Camera setDistance(double distance) {
        this.distance = distance;
        return this;
    }



    public Camera(Point location, Vector up, Vector to) {
        this.location = location;
        this.up = up;
        this.to = to;
    }

    private Camera() {
    }

    //nX-sum of columns
    //nY-sum of lines
    //j-column
    //i-line
    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
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
            camera.location = point_location;
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
            camera.width = width;
            camera.height = height;
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
            if (camera.location == null) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "location");
            }
            if (camera.to == null || camera.up == null || camera.right == null) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "direction vectors");
            }
            if (camera.width == 0.0) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "view plane width");
            }
            if (camera.height == 0.0) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "view plane height");
            }
            if (camera.distance == 0.0) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "view plane distance");
            }
            return (Camera) camera.clone();
        }
    }

}