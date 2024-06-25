package renderer;

import primitives.Color;
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

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

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
            Point Pc = Plocation.add(to.scale(distance));
            double Ry = _height / nY;
            double Rx = _width / nX;

            double Yi = -1 * (i - (nY - 1) / 2.0) * Ry;
            double Xj = (j - (nX - 1) / 2.0) * Rx;

            Point Pij = Pc;
            if (!isZero(Xj)) {
                Pij = Pij.add(right.scale(Xj));
            }

            if (!isZero(Yi)) {
                Pij = Pij.add(up.scale(Yi));
            }

            return new Ray(Plocation, Pij.subtract(Plocation));
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
     * Casts a ray through a specific pixel and traces it to get the color at the intersection point.
     *
     * @param j the column index of the pixel.
     * @param i the row index of the pixel.
     * @return the color at the intersection point.
     */
    private Color castRay(int nx, int ny,int j, int i) {
        Color color= rayTracer.traceRay(constructRay(nx, ny, j, i));
        imageWriter.writePixel(j, i, color);
        return color;
    }

    /**
     * Renders an image by casting rays through all the pixels on the view plane.
     *
     * @return the current Camera object.
     */
    public Camera renderImage() {
        int nx = imageWriter.getNx();
        int ny = imageWriter.getNy();

        for (int i = 0; i < nx; ++i) {
            for (int j = 0; j < ny; ++j) {
                castRay(nx, ny, j, i);
            }
        }
        return this;
    }
    /**
     * Prints a grid on the image.
     *
     * @param interval the interval between grid lines.
     * @param color the color of the grid lines.
     * @return the current Camera object.
     */
    public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < imageWriter.getNx(); i++) {
            for (int j = 0; j < imageWriter.getNy(); j++) {
                // Write the grid line color to the pixel
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(i, j, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a file.
     */
    public void writeToImage() {
        this.imageWriter.writeToImage();
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
            if (camera.imageWriter == null) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "imageWriter");
            }
            if (camera.rayTracer == null) {
                throw new MissingResourceException("Missing rendering data", Camera.class.getName(), "rayTracer");
            }
            camera.viewPlanePC = camera.Plocation.add(camera.to.scale(camera.distance));
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException cloneExc) {
                throw new RuntimeException(cloneExc);
            }
        }

        /**
         * Sets the RayTracer for the Camera.
         *
         * @param simpleRayTracer the RayTracer object.
         * @return the current Builder instance.
         */
        public Builder setRayTracer(SimpleRayTracer simpleRayTracer) {
            camera.rayTracer = simpleRayTracer;
            return this;
        }

        /**
         * Sets the ImageWriter for the Camera.
         *
         * @param base_render_test the ImageWriter object.
         * @return the current Builder instance.
         */
        public Builder setImageWriter(ImageWriter base_render_test) {
            camera.imageWriter = base_render_test;
            return this;
        }
    }
}
