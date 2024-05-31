package geometries;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTests {
    @Test
    public void testAddEmpty() {
        // BVA - מקרה קצה עליון תחתון
        Geometries geometries = new Geometries();
        assertTrue(geometries.getIntersectables().isEmpty(), "The list should be empty.");

}