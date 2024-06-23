package renderer;

import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.*;
import scene.Scene;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for the Camera's ray construction with various geometries.
 * This class tests the number of intersections between rays constructed from the Camera
 * and different geometric bodies such as Sphere, Plane, and Triangle.
 */
class CameraRayIntersectionsIntegrationTests {

    /**
     * Builder for the first camera configuration.
     */
    private final Camera.Builder camera1Builder = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 1, 1))
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0,0,-1), new Vector(0,1,0))
            .setVpSize(3,3)
            .setVpDistance(1);

    /**
     * Builder for the second camera configuration.
     */
    private final Camera.Builder camera2Builder = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 1, 1))
            .setLocation(new Point(0,0,0.5))
            .setDirection(new Vector(0,0,-1), new Vector(0,1,0))
            .setVpSize(3,3)
            .setVpDistance(1);

    /**
     * The first camera configuration.
     */
    private final Camera camera1 = camera1Builder.build();

    /**
     * The second camera configuration.
     */
    private final Camera camera2 = camera2Builder.build();

    /**
     * Test helper function to count the intersections and compare with expected value.
     *
     * @param camera the Camera used for the test
     * @param geometry the 3D body to test the integration of the camera with
     * @param expected the expected number of intersections
     * @param testMessage the message for the assertion
     */
    private void assertCountIntersections(Camera camera, Intersectable geometry, int expected, String testMessage) {
        int countIntersections = 0;
        int nX = 3;
        int nY = 3;

        for(int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = camera.constructRay(nX, nY, j, i);
                List<Point> intersections = geometry.findIntersections(ray);
                if (intersections != null) {
                    countIntersections += intersections.size();
                }
            }
        }

        assertEquals(expected, countIntersections, testMessage);
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Sphere intersections.
     */
    @Test
    public void cameraRaySphereIntegration() {
        // ============ Equivalence Partitions Tests ==============

        Sphere sphere = new Sphere(new Point(0, 0, -3), 1);

        // TC01: Small Sphere 2 points
        assertCountIntersections(camera1, sphere, 2, "TC01: Small Sphere 2 points");
        // TC02: Big Sphere 18 points
        sphere = new Sphere(new Point(0, 0, -2.5), 2.5);
        assertCountIntersections(camera2, sphere, 18, "TC02: Big Sphere 18 points");
        // TC03: Medium Sphere 10 points
        sphere = new Sphere(new Point(0, 0, -2), 2);
        assertCountIntersections(camera2, sphere, 10, "TC03: Medium Sphere 10 points");
        // TC04: Inside Sphere 9 points
        sphere = new Sphere(new Point(0, 0, -0.5), 4);
        assertCountIntersections(camera1, sphere, 9, "TC04: Inside Sphere 9 points");
        // TC05: Beyond Sphere 0 points
        sphere = new Sphere(new Point(0, 0, 1), 1);
        assertCountIntersections(camera1, sphere, 0, "TC05: Beyond Sphere 0 points");
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Plane intersections.
     */
    @Test
    public void cameraRayPlaneIntegration() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Plane against camera 9 points
        Plane plane = new Plane(new Vector(0, 0, 3), new Point(0, 0, -4));
        assertCountIntersections(camera1, plane, 9, "TC01: Plane against camera 9 points");
        // TC02: Plane with small angle 9 points
        plane = new Plane(new Vector(0, 0.5, -1), new Point(0, 2, -1));
        assertCountIntersections(camera1, plane, 9, "TC02: Plane with small angle 9 points");
        // TC03: Plane parallel to lower rays 6 points
        plane = new Plane(new Point(0, 0, -4), new Point(0, 1, -1), new Point(1, 0, -4));
        assertCountIntersections(camera1, plane, 6, "TC03: Plane parallel to lower rays 6 points");
        // TC04: Beyond Plane 0 points
        plane = new Plane(new Vector(0, 0, 4), new Point(0, 0, 1));
        assertCountIntersections(camera1, plane, 0, "TC04: Beyond Plane 0 points");
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Triangle intersections.
     */
    @Test
    public void cameraRayTriangleIntegration() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Small triangle 1 point
        Triangle triangle = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        assertCountIntersections(camera1, triangle, 1, "TC01: Small triangle 1 point");
        // TC02: Medium triangle 2 points
        triangle = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        assertCountIntersections(camera1, triangle, 2, "TC02: Medium triangle 2 points");
    }
}
