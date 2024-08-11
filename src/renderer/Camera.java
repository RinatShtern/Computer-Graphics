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

    private Point Plocation; // The location of the camera
    private Vector right; // The right direction vector of the camera
    private Vector up; // The up direction vector of the camera
    private Vector to; // The forward direction vector of the camera
    private double _height = 0; // The height of the view plane
    private double _width = 0; // The width of the view plane
    private double distance = 0; // The distance between the camera and the view plane
    private Point viewPlanePC; // The center point of the view plane

    private ImageWriter imageWriter; // The object responsible for writing the image
    private RayTracerBase rayTracer; // The ray tracer used for rendering the image
    private static boolean adaptive = false; // Flag to enable or disable adaptive super-sampling
    private static int numOfThreads = 1; // The number of threads to be used for rendering
    private static int antiAliasing = 1; // The number of rays used for anti-aliasing
    private double printInterval = 0; // The interval for printing progress
    private int threadsCount = 0; // The number of threads currently in use

    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;

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
     * Gets the forward direction vector of the camera.
     *
     * @return the forward direction vector.
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
     * @param up       the up direction vector of the camera.
     * @param to       the forward direction vector of the camera.
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
     * @param width  the width of the view plane.
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
     * @param j  the column index of the pixel.
     * @param i  the row index of the pixel.
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
     * Casts a ray from the camera and colors a pixel.
     *
     * @param nX   resolution on X axis (number of pixels in row).
     * @param nY   resolution on Y axis (number of pixels in column).
     * @param col  pixel's column number (pixel index in row).
     * @param row  pixel's row number (pixel index in column).
     */
    private void castRay(int nX, int nY, int col, int row) {
        Color color = Color.BLACK;
        if (antiAliasing > 1) {
            color = SuperSampling(nX, nY, col, row, antiAliasing, false);
        } else if (adaptive) {
            List<Ray> rays = constructRays(nX, nY, col, row);
            color = rayTracer.TraceRays(rays);
        } else {
            color = rayTracer.traceRay(constructRay(nX, nY, col, row));
        }
        imageWriter.writePixel(col, row, color);
        pixelManager.pixelDone();
    }

    /**
     * Renders an image by casting rays through all the pixels on the view plane.
     *
     * @return the current Camera object.
     */
    public Camera renderImage() {
        int nx = imageWriter.getNx();
        int ny = imageWriter.getNy();
        pixelManager = new PixelManager(ny, nx, printInterval);
        if (threadsCount == 0)
            for (int i = 0; i < ny; ++i)
                for (int j = 0; j < nx; ++j)
                    castRay(nx, ny, j, i);
        else {
            // Multi-threading option
            var threads = new LinkedList<Thread>(); // List of threads
            while (threadsCount-- > 0) // Add appropriate number of threads
                threads.add(new Thread(() -> {
                    PixelManager.Pixel pixel; // Current pixel(row, col)
                    // Allocate pixel(row, col) in loop until there are no more pixels
                    while ((pixel = pixelManager.nextPixel()) != null)
                        // Cast ray through pixel (and color it – inside castRay)
                        castRay(nx, ny, pixel.col(), pixel.row());
                }));
            // Start all the threads
            for (var thread : threads) thread.start();
            // Wait until all the threads have finished
            try {
                for (var thread : threads) thread.join();
            } catch (InterruptedException ignore) {
            }
        }
        return this;
    }

    /**
     * Constructs rays through a specific pixel on the view plane for anti-aliasing.
     *
     * @param nX the number of columns on the view plane.
     * @param nY the number of rows on the view plane.
     * @param j  the column index of the pixel.
     * @param i  the row index of the pixel.
     * @return the list of constructed Rays.
     */
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
     * Checks the color of the pixel with the help of individual rays, averages between them, and
     * if necessary, continues to send beams of rays recursively.
     *
     * @param nX          the number of pixels by length.
     * @param nY          the number of pixels by width.
     * @param j           the position of the pixel relative to the y-axis.
     * @param i           the position of the pixel relative to the x-axis.
     * @param numOfRays   the number of rays sent.
     * @param adaptiveAliasing whether to use adaptive aliasing.
     * @return the color of the pixel.
     */
    private Color SuperSampling(int nX, int nY, int j, int i, int numOfRays, boolean adaptiveAliasing) {
        // קבלת הוקטורים ימינה ולמעלה של המצלמה
        Vector Vright = right;
        Vector Vup = up;

        // קבלת המיקום של המצלמה
        Point cameraLoc = this.getLocation();

        // nX: מספר העמודות בפיקסלים בתמונה (רזולוציה בכיוון האופקי)
        // nY: מספר השורות בפיקסלים בתמונה (רזולוציה בכיוון האנכי)
        // j: אינדקס העמודה של הפיקסל הנוכחי שבו דוגמים
        // i: אינדקס השורה של הפיקסל הנוכחי שבו דוגמים
        // numOfRays: מספר הקרניים שנשלחות לכל פיקסל לצורך דגימה
        // adaptiveAliasing: משתנה בוליאני שמצביע האם לבצע סופר-סמפולינג אדפטיבי או רגיל

        // חישוב מספר הקרניים בכל שורה ועמודה בתוך הפיקסל
        int numOfRaysInRowCol = (int) Math.floor(Math.sqrt(numOfRays));

        // אם מספר הקרניים הוא 1, מבצעים דגימה רגילה (ללא סופר-סמפולינג)
        if (numOfRaysInRowCol == 1)
            return rayTracer.traceRay(constructRayThroughPixel(nX, nY, j, i));

        // חישוב נקודת המרכז של הפיקסל הנוכחי
        Point pIJ = getCenterOfPixel(nX, nY, j, i);

        // חישוב יחס הגובה של הפיקסל (rY)
        double rY = alignZero(_height / nY);
        // חישוב יחס הרוחב של הפיקסל (rX)
        double rX = alignZero(_width / nX);

        // חישוב יחס הגובה והרוחב של תת-הפיקסלים בתוך הפיקסל
        double PRy = rY / numOfRaysInRowCol;
        double PRx = rX / numOfRaysInRowCol;

        // אם בוצע סופר-סמפולינג אדפטיבי, מפעילים את הפונקציה המתאימה לכך
        if (adaptiveAliasing)
            return rayTracer.AdaptiveSuperSamplingRec(pIJ, rX, rY, PRx, PRy, cameraLoc, Vright, Vup, null);
        else
            // אחרת, מבצעים סופר-סמפולינג רגיל
            return rayTracer.RegularSuperSampling(pIJ, rX, rY, PRx, PRy, cameraLoc, Vright, Vup, null);
    }
    /**
     * Constructs a ray through a specific pixel in the view plane.
     * nX and nY create the resolution.
     *
     * @param nX the number of pixels in the width of the view plane.
     * @param nY the number of pixels in the height of the view plane.
     * @param j  the index row in the view plane.
     * @param i  the index column in the view plane.
     * @return the ray that goes through the pixel (j, i).
     */
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
        Point pIJ = getCenterOfPixel(nX, nY, j, i); // center point of the pixel

        // Vi,j = Pi,j - P0, the direction of the ray to the pixel(j, i)
        Vector vIJ = pIJ.subtract(Plocation);
        return new Ray(Plocation, vIJ);
    }

    /**
     * Gets the center point of the pixel in the view plane.
     *
     * @param nX the number of pixels in the width of the view plane.
     * @param nY the number of pixels in the height of the view plane.
     * @param j  the index row in the view plane.
     * @param i  the index column in the view plane.
     * @return the center point of the pixel.
     */
    private Point getCenterOfPixel(int nX, int nY, int j, int i) {
        // calculate the ratio of the pixel by the height and by the width of the view plane
        double rY = alignZero(_height / nY); // the ratio Ry = h/Ny, the height of the pixel
        double rX = alignZero(_width / nX);  // the ratio Rx = w/Nx, the width of the pixel

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
     * @param color    the color of the grid lines.
     * @return the current Camera object.
     */
    public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < imageWriter.getNx(); i++) {
            for (int j = 0; j < imageWriter.getNy(); j++) {
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
         * Sets the direction for the camera with a target point.
         *
         * @param to  target point where the camera is directed to.
         * @param up  where the "up" direction of the camera is.
         * @return the builder itself.
         */
        public Builder setDirection(Point to, Vector up) {
            // Check if vectors are aligned
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
         * @param to  the forward direction vector.
         * @param up  the up direction vector.
         * @return the current Builder instance.
         */
        public Builder setDirection(Vector to, Vector up) {
            if (to == null || up == null) {
                throw new IllegalArgumentException("Direction vectors cannot be null");
            }
            camera.to = to.normalize();
            camera.up = up.normalize();
            camera.right = camera.to.crossProduct(camera.up).normalize();
            return this;
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  the width of the view plane.
         * @param height the height of the view plane.
         * @return the current Builder instance.
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be positive numbers");
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
                throw new IllegalArgumentException("Distance must be a positive number");
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
            if (camera.to == null || camera.up == null) {
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
            if (simpleRayTracer == null) {
                throw new IllegalArgumentException("RayTracer cannot be null");
            }
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
            if (base_render_test == null) {
                throw new IllegalArgumentException("ImageWriter cannot be null");
            }
            camera.imageWriter = base_render_test;
            return this;
        }

        /**
         * Enables or disables adaptive super-sampling.
         *
         * @param adaptive1 a boolean flag to enable or disable adaptive super-sampling.
         * @return the current Builder instance.
         */
        public Builder setadaptive(boolean adaptive1) {
            adaptive = adaptive1;
            return this;
        }

        /**
         * Sets the number of threads for multi-threading.
         *
         * @param threadsCount the number of threads.
         * @return the current Builder instance.
         */
        public Builder setMultiThreading(int threadsCount) {
            numOfThreads = threadsCount;
            return this;
        }

        /**
         * Sets the number of rays for anti-aliasing.
         *
         * @param nRays the number of rays for anti-aliasing.
         * @return the current Builder instance.
         */
        public Builder setAntiAliasing(int nRays) {
            antiAliasing = nRays;
            return this;
        }
    }

}
