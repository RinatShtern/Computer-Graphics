package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Camera class represents a camera in 3D space.
 * It constructs rays through pixels on the view plane for rendering images.
 */
public class Camera implements Cloneable {

    private Point Plocation;
    private Vector right;
    private Vector up;
    private Vector to;
    private double _height = 0;
    private double _width = 0;
    private double distance = 0;
    private Point viewPlanePC;

    /**
     * Gets the camera location.
     *
     * @return the camera location.
     */
    public Point getLocation() {
        return Plocation;
    }

    /**
     * Gets the right direction vector of the camera.
     *
     * @return the right direction vector.
     */
    public Vector getRight() {
        return right;
    }

    /**
     * Gets the up direction vector of the camera.
     *
     * @return the up direction vector.
     */
    public Vector getUp() {
        return up;
    }

    /**
     * Gets the to direction vector of the camera.
     *
     * @return the to direction vector.
     */
    public Vector getTo() {
        return to;
    }

    /**
     * Gets the view plane height.
     *
     * @return the view plane height.
     */
    public double getHeight() {
        return _height;
    }

    /**
     * Gets the view plane width.
     *
     * @return the view plane width.
     */
    public double getWidth() {
        return _width;
    }

    /**
     * Gets the distance between the camera and the view plane.
     *
     * @return the distance between the camera and the view plane.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Constructs a Camera object with a given location, up vector, and to vector.
     *
     * @param location the location of the camera.
     * @param up the up direction vector of the camera.
     * @param to the to direction vector of the camera.
     */
    public Camera(Point location, Vector up, Vector to) {
        this.Plocation = location;

        if (!isZero(up.dotProduct(to))) {
            throw new IllegalArgumentException("vup and vto are not orthogonal");
        }
        this.up = up.normalize();
        this.to = to.normalize();
        this.right = to.crossProduct(up);
    }

    /**
     * Default constructor for the Camera class.
     */
    private Camera() {
    }

    /**
     * Sets the view plane distance.
     *
     * @param distance the distance between the camera and the view plane.
     * @return the current Camera object.
     */
    public Camera setVPDistance(double distance) {
        this.distance = distance;
        return this;
    }

    /**
     * Sets the view plane size.
     *
     * @param width the width of the view plane.
     * @param height the height of the view plane.
     * @return the current Camera object.
     */
    public Camera setVPSize(double width, double height) {
        _width = width;
        _height = height;
        return this;
    }

    /**
     * Constructs a ray through a specific pixel on the view plane.
     *
     * @param nX the number of columns on the view plane.
     * @param nY the number of rows on the view plane.
     * @param j the column index of the pixel.
     * @param i the row index of the pixel.
     * @return the constructed Ray.
     */
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

    /**
     * Creates a new Builder instance for constructing a Camera object.
     *
     * @return a new Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * The Builder class for constructing Camera objects using the Builder design pattern.
     */
    public static class Builder {
        private final Camera camera = new Camera();

        /**
         * Default constructor for the Builder class.
         */
        public Builder() {
        }

        /**
         * Sets the location of the Camera.
         *
         * @param point_location the location of the Camera.
         * @return the current Builder instance.
         */
        public Builder setLocation(Point point_location) {
            if (point_location == null) {
                throw new IllegalArgumentException("Location cannot be null");
            }
            camera.Plocation = point_location;
            return this;
        }

        /**
         * Sets the direction vectors of the Camera.
         *
         * @param to the to direction vector.
         * @param up the up direction vector.
         * @return the current Builder instance.
         */
        public Builder setDirection(Vector to, Vector up) {
            if (to == null || up == null && !isZero(to.dotProduct(up))) {
                throw new IllegalArgumentException("Vectors cannot be null");
            }
            camera.to = to.normalize();
            camera.right = camera.to.crossProduct(up).normalize();
            camera.up = up.normalize();
            return this;
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width the width of the view plane.
         * @param height the height of the view plane.
         * @return the current Builder instance.
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            camera._width = width;
            camera._height = height;
            return this;
        }

        /**
         * Sets the distance between the Camera and the view plane.
         *
         * @param distance the distance between the Camera and the view plane.
         * @return the current Builder instance.
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Builds and returns the Camera object.
         *
         * @return the constructed Camera object.
         */
        public Camera build() {
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
            camera.viewPlanePC = camera.Plocation.add(camera.to.scale(camera.distance));
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException cloneExc) {
                throw new RuntimeException(cloneExc);
            }
        }
    }
}
