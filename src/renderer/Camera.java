package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
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
    private static boolean adaptive = false;
    private static int numOfThreads = 1;
    private static int antiAliasing = 1;


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
     * Renders the image using the current image writer and ray tracer.
     * The ray tracer find the color and the image writer colors the pixels
     *
     * @return This camera instance.
     * @throws UnsupportedOperationException If either the image writer or the ray tracer is not initialized.
     */
    public Camera renderImagepixel() {
        // Check if all required camera data is available
        if (Plocation == null || right == null
                || up == null || to == null || distance == 0
                || _width == 0 || _height == 0 || viewPlanePC == null
                || imageWriter == null || rayTracer == null) {
            throw new MissingResourceException("Missing camera data", Camera.class.getName(), null);
        }
        // Get the number of pixels in X and Y directions from the image writer
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        // Initialize the Pixel class with the number of rows, columns, and total pixels
        Pixel.initialize(nY, nX, 1);

        // Check if adaptive mode is enabled
        if (!adaptive) {
            // Render the image using regular super-sampling (non-adaptive)
            // Create multiple threads to process the pixels in parallel
            while (numOfThreads-- > 0) {
                new Thread(() -> {
                    // Iterate over each pixel in the image
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone()) {
                        // Construct rays for the current pixel and trace them using the ray tracer
                        List<Ray> rays = constructRays(nX, nY, pixel.col, pixel.row);
                        Color pixelColor = rayTracer.TraceRays(rays);
                        // Write the pixel color to the image writer
                        imageWriter.writePixel(pixel.col, pixel.row, pixelColor);
                    }
                }).start();
            }
            // Wait for all the threads to finish processing the pixels
            Pixel.waitToFinish();
        }
        else {
            // Render the image using adaptive super-sampling
            // Create multiple threads to process the pixels in parallel
            while (numOfThreads-- > 0) {
                new Thread(() -> {
                    // Iterate over each pixel in the image
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone()) {
                        // Apply adaptive super-sampling to determine the pixel color
                        Color pixelColor = SuperSampling(nX, nY, pixel.col, pixel.row, antiAliasing, false);
                        // Write the pixel color to the image writer
                        imageWriter.writePixel(pixel.col, pixel.row, pixelColor);
                    }
                }).start();
            }
            // Wait for all the threads to finish processing the pixels
            Pixel.waitToFinish();
        }
        // Return the camera object
        return this;
    }
    public List<Ray> constructRays(int nX, int nY, int j, int i) {
        List<Ray> rays = new LinkedList<>();
        Point centralPixel = getCenterOfPixel(nX, nY, j, i);
        double rY = _height / nY / antiAliasing;
        double rX = _width / nX / antiAliasing;
        // Variables to store the X and Y offsets of each sub-pixel within the anti-aliasing grid
        double x, y;

        for (int rowNumber = 0; rowNumber < antiAliasing; rowNumber++) {
            for (int colNumber = 0; colNumber < antiAliasing; colNumber++) {
                // Calculate the X and Y offsets for the current sub-pixel
                y = -(rowNumber - (antiAliasing - 1d) / 2) * rY;
                x = (colNumber - (antiAliasing - 1d) / 2) * rX;
                // Calculate the position of the current sub-pixel within the pixel
                Point pIJ = centralPixel;
                if (y != 0) pIJ = pIJ.add(up.scale(y));
                if (x != 0) pIJ = pIJ.add(right.scale(x));
                // Construct a ray from the camera position to the current sub-pixel
                rays.add(new Ray(Plocation, pIJ.subtract(Plocation)));
            }
        }
        return rays;
    }
    /**
     * Checks the color of the pixel with the help of individual rays and averages between them and only
     * if necessary continues to send beams of rays in recursion
     * @param nX amount of pixels by length
     * @param nY amount of pixels by width
     * @param j The position of the pixel relative to the y-axis
     * @param i The position of the pixel relative to the x-axis
     * @param numOfRays The amount of rays sent
     * @return Pixel color
     */
    private Color SuperSampling(int nX, int nY, int j, int i,  int numOfRays, boolean adaptiveAlising)  {
        // Get the right and up vectors of the camera
        Vector Vright = right;
        Vector Vup = up;
        // Get the location of the camera
        Point cameraLoc = this.getLocation();
        // Calculate the number of rays in each row and column
        int numOfRaysInRowCol = (int)Math.floor(Math.sqrt(numOfRays));
        // If the number of rays is 1, perform regular ray tracing
        if(numOfRaysInRowCol == 1)
            return rayTracer.traceRay(constructRayThroughPixel(nX, nY, j, i));
        // Calculate the center point of the current pixel
        Point pIJ = getCenterOfPixel(nX, nY, j, i);
        // Calculate the height and width ratios of the pixel
        double rY = alignZero(_height / nY);
        double rX = alignZero(_width / nX);

        // Calculate the pixel row and column ratios
        double PRy = rY/numOfRaysInRowCol;
        double PRx = rX/numOfRaysInRowCol;

        if (adaptiveAlising)
            return rayTracer.AdaptiveSuperSamplingRec(pIJ, rX, rY, PRx, PRy,cameraLoc,Vright, Vup,null);
        else
            return rayTracer.RegularSuperSampling(pIJ, rX, rY, PRx, PRy,cameraLoc,Vright, Vup,null);
    }


     /**
     * construct ray through a pixel in the view plane
     * nX and nY create the resolution
     * @param nX number of pixels in the width of the view plane
     * @param nY number of pixels in the height of the view plane
     * @param j  index row in the view plane
     * @param i  index column in the view plane
     * @return ray that goes through the pixel (j, i)  Ray(p0, Vi,j)
     */
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
        Point pIJ = getCenterOfPixel(nX, nY, j, i); // center point of the pixel

        //Vi,j = Pi,j - P0, the direction of the ray to the pixel(j, i)
        Vector vIJ = pIJ.subtract(Plocation);
        return new Ray(Plocation, vIJ);
    }
    /**
     * get the center point of the pixel in the view plane
     * @param nX number of pixels in the width of the view plane
     * @param nY number of pixels in the height of the view plane
     * @param j  index row in the view plane
     * @param i  index column in the view plane
     * @return the center point of the pixel
     */
    private Point getCenterOfPixel(int nX, int nY, int j, int i) {
        // calculate the ratio of the pixel by the height and by the width of the view plane
        // the ratio Ry = h/Ny, the height of the pixel
        double rY = alignZero(_height / nY);
        // the ratio Rx = w/Nx, the width of the pixel
        double rX = alignZero(_width / nX);

        // Xj = (j - (Nx -1)/2) * Rx
        double xJ = alignZero((j - ((nX - 1d) / 2d)) * rX);
        // Yi = -(i - (Ny - 1)/2) * Ry
        double yI = alignZero(-(i - ((nY - 1d) / 2d)) * rY);

        Point pIJ = viewPlanePC;

        if (!isZero(xJ)) {
            pIJ = pIJ.add(right.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(up.scale(yI));
        }
        return pIJ;
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
         * set Direction for camera with a target point
         *
         * @param to target point where the camera is directed to
         * @param up where is the "up" direction of the camera to
         * @return the builder itself
         */
        public Builder setDirection(Point to, Vector up) {
            //check if vectors are aligned
            if (camera.Plocation == null) throw new IllegalArgumentException("Please set the camera location first");

            camera.to = to.subtract(camera.Plocation).normalize();
            up = up.normalize();
            camera.right = camera.to.crossProduct(up).normalize();
            camera.up = camera.right.crossProduct(camera.to).normalize();

            return this;
        }
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
            if (camera.to == null || camera.up == null ) {
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

            camera.right = camera.to.crossProduct(camera.up).normalize();

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

        /**
         * set the adaptive
         * @return the Camera object
         */
        public Builder setadaptive(boolean adaptive1) {
            adaptive = adaptive1;
            return this;
        }

        /**
         * set the threadsCount
         * @return the Camera object
         */
        public Builder setMultiThreading(int threadsCount) {
            numOfThreads = threadsCount;
            return this;

        }
        public Builder setAntiAliasing(int nRays){
            antiAliasing = nRays;
            return this;
        }
    }
}
