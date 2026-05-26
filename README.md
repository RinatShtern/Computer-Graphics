# 3D Graphical Scene Renderer (Ray Tracer)

A comprehensive 3D virtual graphical scene simulation engine built from scratch in Java, utilizing advanced Object-Oriented Design (OOD) and software engineering principles. The engine renders complex geometric structures, manages multi-source lighting, and implements sophisticated visual and performance optimizations.

---

## 🌄 Final Rendered Scene
*Below is the visual output of the rendered 3D environment, featuring complex geometric shapes, reflective water surfaces, and realistic atmospheric lighting.*

![Final Rendered Scene](images/final_scene.png)

## 🚀 Core Features

### 1. Scene Composition & Geometry
The engine supports the rendering of complex 3D environments containing multiple geometric structures:
* **Complex Polyhedrons:** Detailed mountains composed of multiple 3D triangles, including dynamic snowcapped peaks.
* **Structural Meshes:** Coastal houses with multi-triangle walls and pyramid-style triangular roofs.
* **Spherical Objects:** Organic vegetation (bushes), complex cloud formations made of overlapping spheres, and realistic falling snowflakes.
* **Reflective Environments:** Dynamic water surfaces capable of reflecting light sources and surrounding landscape elements.

### 2. Multi-Source Lighting System
The rendering engine computes illumination across **5 separate light sources** to generate a balanced, highly atmospheric scene:
* **Dual Spotlights:** Focused light beams targeting the central scenery, emphasizing complex reflections on the water, mountains, and trees.
* **Dual Directional Lights:** One providing unified global ambient-like baseline illumination, and another angled from the upper-right to cast dramatic directional shadows and crisp specular reflections over the water.
* **Ambient Light:** Soft, uniform filling illumination to balance contrast and prevent completely unlit, pitch-black void zones.

---

## 🛠 Advanced Graphical Optimizations

### 🌓 Soft Shadows (Area Lights)
* **The Problem:** Standard hard shadows (point light sources) create binary, unrealistically sharp edges that make 3D scenes look artificial.
* **The Solution:** Implemented an `Area Light` model by expanding the light source to support a radius. Instead of casting a single ray, the engine generates a distributed beam of rays (`getRayBeam`) mapped across a calculated containment plane perpendicular to the light vector.
* **Result:** Realistic, naturally gradient shadows with soft, blended edges based on the percentage of unblocked rays hitting the surface.

### 📸 Anti-Aliasing & Super-Sampling
* **The Problem:** Aliasing artifacts and jagged, pixelated edges ("jaggies") appearing on non-linear or diagonal geometric boundaries due to single-point pixel center sampling.
* **The Solution:** Implemented super-sampling through the pixel grid (`constructRays`). The system fires multiple randomized and distributed sub-pixel rays through each pixel, averaging the computed color returns (`castRay`) to blend color transitions smoothly.

---

## ⚡ Performance & Execution Upgrades

To combat the massive computational overhead of multi-ray rendering, the project implements critical performance optimizations:

### 🧩 Adaptive Super-Sampling
Rather than blindly casting a dense grid of rays for every single pixel, a recursive algorithm evaluates the color similarity of the pixel's corner boundaries. 

* **If the boundary colors match:** The area skips further division, saving immense computation time.
* **If they differ:** The pixel recursively subdivides into sub-quadrants until a minimum threshold size is met or uniform coloring is achieved.

#### Code Implementation Snippet:
```java
// Recursive adaptive super-sampling evaluation
boolean isAllEquals = true;
primitives.Color tempColor = colorList.get(0);

// Check if all colors in the color list are almost equal
for (primitives.Color color : colorList) {
    if (!tempColor.isAlmostEquals(color)) {
        isAllEquals = false;
    }
}

if (isAllEquals && colorList.size() > 1) {
    // All colors are equal; return the first color to optimize execution
    return tempColor;
}
