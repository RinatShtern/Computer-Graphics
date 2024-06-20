package primitives;

import java.util.List;
import java.util.Objects;

/**
 * Represents a ray in the 3D space.
 * A ray is defined by its starting point (head) and a direction vector.
 */
public class Ray {

    final private Point head; // The starting point of the ray
    final private Vector direction; // The direction vector of the ray

    /**
     * Constructs a ray with the given starting point and direction vector.
     *
     * @param p the starting point of the ray
     * @param v the direction vector of the ray
     */
    public Ray(Point p, Vector v) {
        this.head = p;
        this.direction = v.normalize(); // Normalize the direction vector
    }

    /**
     * Retrieves a string representation of the ray.
     *
     * @return a string representation of the ray
     */
    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }

    /**
     * Checks if this ray is equal to another object.
     *
     * @param o the object to compare
     * @return true if the rays are equal, false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ray ray)) return false;

        return Objects.equals(head, ray.head) && Objects.equals(direction, ray.direction);
    }

    /**
     * Calculates the hash code for the ray.
     *
     * @return the hash code for the ray
     */
    @Override
    public int hashCode() {
        int result = Objects.hashCode(head);
        result = 31 * result + Objects.hashCode(direction);
        return result;
    }

    public Vector getDirection() {
        return direction;
    }
    public Point getHead() {
        return head;
    }

    public Point getPoint(double t){
        if(t==0)
            return head;
        return head.add(direction.scale(t));
    }

     public Point findClosestPoint(List<Point> pointList) {
        if (pointList == null || pointList.isEmpty()) {
            return null;
        }

        Point result = null;
        double minDistance = Double.MAX_VALUE; // אתחול לערך הגדול ביותר האפשרי
        double ptDistance;

        for (Point pt : pointList) {
            ptDistance = head.distance(pt);
            if (ptDistance < minDistance) {
                minDistance = ptDistance;
                result = pt;
            }
        }
        return result;
    }

}