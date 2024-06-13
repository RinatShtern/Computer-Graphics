package renderer;

import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CameraRayIntersectionsIntegrationTests {

    /**
     * Test helper function to count the intersections and compare with expected value
     * @param cam camera for the test
     * @param geo 3D body to test the integration of the camera with
     * @param expected number amount of intersections
     */
    private void assertCountIntersections(Camera cam, Intersectable geo, int expected) {
        List<Point> allPoints=null;
        for(int i=0; i<3;i++){
            for(int j=0; j<3;j++){
                Ray ray=cam.constructRay(3,3,j,i);
                List<Point> lst=geo.findIntersections(ray);
                if(lst!=null){
                    if (allPoints==null){
                        allPoints=new LinkedList<>();
                    }
                    allPoints.addAll(lst);
                }
            }
        }
        if(allPoints==null) {
            return;
        }
        else{
            assertEquals(expected,allPoints.size(),
                    "num of intersection points aren't enough ");}
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Sphere intersections
     */
    @Test
    public void cameraRaySphereIntegration() {
        // ============ Equivalence Partitions Tests ==============

        Sphere sphere=new Sphere(new Point(0,0,-3),1);
        Camera camera=new Camera(new Point(0,0,0),new Vector(0,0,-1),new Vector(0,1,0))
                .setVPDistance(1).setVPSize(3,3);
        // TC01: Small Sphere 2 points
        assertCountIntersections(camera,sphere,2);
        // TC02: Big Sphere 18 points
        camera=new Camera(new Point(0,0,0.5),new Vector(0,0,-1),new Vector(0,1,0))
                .setVPDistance(1).setVPSize(3,3);
        sphere=new Sphere(new Point(0,0,-2.5),2.5);
        assertCountIntersections(camera,sphere,18);
        // TC03: Medium Sphere 10 points
        sphere=new Sphere(new Point(0,0,-2),2);
        assertCountIntersections(camera,sphere,10);
        // TC04: Inside Sphere 9 points
        sphere=new Sphere(new Point(0,0,-0.5),4);
        assertCountIntersections(camera,sphere,9);
        // TC05: Beyond Sphere 0 points
        camera=new Camera(new Point(0,0,0),new Vector(0,0,-1),new Vector(0,1,0))
                .setVPDistance(1).setVPSize(3,3);
        sphere=new Sphere(new Point(0,0,1),1);
        assertCountIntersections(camera,sphere,0);

    }

    /**
     * Integration tests of Camera Ray construction with Ray-Plane intersections
     */
    @Test
    public void cameraRayPlaneIntegration() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Plane against camera 9 points
        Camera camera=new Camera(new Point(0,0,0),new Vector(0,0,-1),new Vector(0,1,0)).setVPDistance(1).setVPSize(3,3);
        Plane plane=new Plane(new Vector(0,0,3),new Point(0,0,-4));
        assertCountIntersections(camera,plane,9);
        // TC02: Plane with small angle 9 points
        plane=new Plane(new Vector(0,0.5,-1),new Point(0,2,-1));
        assertCountIntersections(camera,plane,9);
        // TC03: Plane parallel to lower rays 6 points
        plane=new Plane(new Point(0,0,-4),new Point(0,1,-1),new Point(1,0,-4));
        assertCountIntersections(camera,plane,6);
        // TC04: Beyond Plane 0 points
        plane=new Plane(new Vector(0,0,4),new Point(0,0,1));
        assertCountIntersections(camera,plane,0);
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Triangle intersections
     */
    @Test
    public void cameraRayTriangleIntegration() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Small triangle 1 point
        Camera camera=new Camera(new Point(0,0,0),new Vector(0,0,-1),new Vector(0,1,0))
                .setVPDistance(1).setVPSize(3,3);
        Triangle triangle=new Triangle(new Point(0,1,-2),new Point(1,-1,-2),new Point(-1,-1,-2));
        assertCountIntersections(camera,triangle,1);

        // TC02: Medium triangle 2 points
        triangle=new Triangle(new Point(0,20,-2),new Point(1,-1,-2),new Point(-1,-1,-2));
        assertCountIntersections(camera,triangle,2);
    }

}