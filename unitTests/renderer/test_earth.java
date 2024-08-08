package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

public class test_earth {

   @Test
   public void ourPicture() {
      Scene scene = new Scene("Test scene")
              .setBackground(new Color(70, 70, 90))  // שינוי צבע הרקע לצבע קריר יותר
              .setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));  // הוספת תאורה רכה

      Camera.Builder camera = Camera.getBuilder()
              .setRayTracer(new SimpleRayTracer(scene))
              .setLocation(new Point(10000, -30000, 10000)).setVpDistance(10000)
              .setDirection(new Vector(-1, 3, -1).normalize(), new Vector(-1, 3, 10))
              .setVpSize(150, 150);

      Material mountainM1 = new Material().setkD(0.6).setkS(0.4).setnShininess(200),
              mountainM2 = new Material().setkD(0.5).setkS(0.3).setnShininess(150),
              snowM = new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4),
              seaM = new Material().setkD(0.2).setkS(0.9).setnShininess(3000).setShininesR(0.6),
              cloudM = new Material().setkD(0.5).setkS(0.6).setnShininess(3000),
              bushM1 = new Material().setkD(0.2).setkS(0.1).setnShininess(300),
              bushM2 = new Material().setkD(0.3).setkS(0.2).setnShininess(200),
              baseM = new Material().setkD(0.7).setkS(0.3).setnShininess(100);  // חומר בסיס לקרקע

      // Sun
      Material sunM = new Material().setkD(0.2).setkS(0.2).setnShininess(200).setkT(0.6);
      scene._geometries.add(new Sphere(new Point(-100, 140, 100), 40d).setEmission(new Color(251, 58, 16)).setMaterial(sunM));
      // Snow
      Material snowMaterial = new Material().setkD(0.2).setkS(0.8).setnShininess(100).setkT(0.3);
      Color snowColor = new Color(255, 255, 255);

      // Add snow up to the tree line
      int snowballs = 100;
      for (int i = 0; i < snowballs; i++) {
         double x = Math.random() * 2000 - 1000;
         double y = Math.random() * 2000 - 1000;
         double z = Math.random() * 500;  // Limit snow height
         if (z <= 500) {  // Adjusted the tree line height to make sure snowballs are visible
            double radius = Math.random() * 10 + 5;
            scene._geometries.add(new Sphere(new Point(x, y, z), radius)
                    .setEmission(snowColor)
                    .setMaterial(snowMaterial));
         }
      }

      //--------------mountains--------------------
      // Only keeping the back mountains
      // Triangle 6
      scene._geometries.add(new Triangle(new Point(-150, 80, 160), new Point(-160, 130, 0), //
              new Point(-250, 70, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      scene._geometries.add(new Triangle(new Point(-150, 80, 160), new Point(-160, 130, 0), //
              new Point(-30, 60, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      scene._geometries.add(new Triangle(new Point(-150, 80, 160), new Point(-130, 20, 0), //
              new Point(-250, 70, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      scene._geometries.add(new Triangle(new Point(-150, 80, 160), new Point(-130, 20, 0), //
              new Point(-30, 60, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));

      // Snow 6
      scene._geometries.add(new Triangle(new Point(-108, 73.12, 105), new Point(-153.75, 99, 100), //
              new Point(-150, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));
      scene._geometries.add(new Triangle(new Point(-108, 73.12, 105), new Point(-142.5, 57, 100), //
              new Point(-150, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));
      scene._geometries.add(new Triangle(new Point(-185, 76.56, 105), new Point(-153.75, 99, 100), //
              new Point(-150, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));
      scene._geometries.add(new Triangle(new Point(-185, 76.56, 105), new Point(-142.5, 57, 100), //
              new Point(-150, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));

      // Triangle 9
      scene._geometries.add(new Triangle(new Point(150, 80, 160), new Point(160, 130, 0), //
              new Point(250, 70, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      scene._geometries.add(new Triangle(new Point(150, 80, 160), new Point(160, 130, 0), //
              new Point(30, 60, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      scene._geometries.add(new Triangle(new Point(150, 80, 160), new Point(130, 20, 0), //
              new Point(250, 70, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      scene._geometries.add(new Triangle(new Point(150, 80, 160), new Point(130, 20, 0), //
              new Point(30, 60, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      // Snow 9
      scene._geometries.add(new Triangle(new Point(185, 76.56, 105), new Point(153.75, 99, 100), //
              new Point(150, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));
      scene._geometries.add(new Triangle(new Point(185, 76.56, 105), new Point(142.5, 57, 100), //
              new Point(150, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));
      scene._geometries.add(new Triangle(new Point(108, 73.12, 105), new Point(153.75, 99, 100), //
              new Point(150, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));
      scene._geometries.add(new Triangle(new Point(108, 73.12, 105), new Point(142.5, 57, 100), //
              new Point(150, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));

      // Triangle 10
      scene._geometries.add(new Triangle(new Point(0, 80, 160), new Point(0, 100, 0), //
              new Point(90, 80, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      scene._geometries.add(new Triangle(new Point(0, 80, 160), new Point(0, 100, 0), //
              new Point(-90, 80, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      scene._geometries.add(new Triangle(new Point(0, 80, 160), new Point(0, 50, 0), //
              new Point(90, 80, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      scene._geometries.add(new Triangle(new Point(0, 80, 160), new Point(0, 50, 0), //
              new Point(-90, 80, 0)).setEmission(new Color(92, 72, 39)).setMaterial(mountainM2));
      // Snow 10
      scene._geometries.add(new Triangle(new Point(-29, 80, 110), new Point(0, 88, 100), //
              new Point(0, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));
      scene._geometries.add(new Triangle(new Point(-29, 80, 110), new Point(0, 68, 100), //
              new Point(0, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));
      scene._geometries.add(new Triangle(new Point(29, 80, 110), new Point(0, 88, 100), //
              new Point(0, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));
      scene._geometries.add(new Triangle(new Point(29, 80, 110), new Point(0, 68, 100), //
              new Point(0, 80, 160)).setEmission(new Color(250, 245, 245)).setMaterial(snowM));

      //-----------End mountains-------------------

      // Earth
      scene._geometries.add(new Triangle(new Point(0, 140, 0), new Point(-600, -45, 0), //
              new Point(600, -45, 0)).setEmission(new Color(92, 73, 57)).setMaterial
              (new Material().setkD(0.5).setkS(0.5).setnShininess(30)));  // יישום חומר הבסיס לקרקע

      // Sea
      scene._geometries.add(new Triangle(new Point(10000, 0, 0), new Point(-10000, 0, 0), //
              new Point(0, -10000, 0)).setEmission(new Color(0, 25, 50)).setMaterial(seaM));

      // Sky
      scene._geometries.add(new Triangle(new Point(10000, 10000, 0), new Point(-10000, 10000, 0), //
              new Point(0, 10000, 100000)).setEmission(new Color(191, 41, 99)));

      // ---------------Clouds---------------
      Color cloudColor = new Color(169, 169, 169);  // שינוי צבע העננים לאפור כהה יותר

      // Cloud 1
      scene._geometries.add(new Sphere(new Point(165, 165, 115), 30d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(140, 165, 105), 20d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(190, 165, 105), 20d).setEmission(cloudColor).setMaterial(cloudM));
      // Cloud 2
      scene._geometries.add(new Sphere(new Point(-215, 225, 135), 30d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(-190, 225, 125), 20d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(-240, 225, 125), 20d).setEmission(cloudColor).setMaterial(cloudM));
      // Cloud 3
      scene._geometries.add(new Sphere(new Point(35, 165, 165), 30d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(10, 165, 155), 20d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(60, 165, 155), 20d).setEmission(cloudColor).setMaterial(cloudM));
      // Cloud 3
      scene._geometries.add(new Sphere(new Point(-245, 5, 135), 30d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(-220, 5, 125), 20d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(-270, 5, 125), 20d).setEmission(cloudColor).setMaterial(cloudM));

      //-------------End clouds---------------

      // Bushes
      scene._geometries.add(new Sphere(new Point(225, -25, -5), 22d).setEmission(new Color(0, 100, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(172, -20, -5), 15d).setEmission(new Color(34, 139, 34)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(130, -15, -5), 24d).setEmission(new Color(0, 128, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(160, -20, -5), 10d).setEmission(new Color(107, 142, 35)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(100, -20, -5), 23d).setEmission(new Color(0, 100, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(30, -19, -5), 14d).setEmission(new Color(34, 139, 34)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(-24, -25, -5), 17d).setEmission(new Color(0, 128, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(-50, -20, -5), 15d).setEmission(new Color(107, 142, 35)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(-100, -16, -5), 23d).setEmission(new Color(0, 100, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(-130, -25, -5), 18d).setEmission(new Color(34, 139, 34)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(-165, -20, -5), 23d).setEmission(new Color(0, 128, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(-257, -22, -5), 19d).setEmission(new Color(107, 142, 35)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(-233, -24, -5), 20d).setEmission(new Color(0, 100, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(-246, -25, -5), 22d).setEmission(new Color(34, 139, 34)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(-220, -20, -5), 15d).setEmission(new Color(0, 128, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(-182, -15, -5), 21d).setEmission(new Color(107, 142, 35)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(-147, -20, -5), 10d).setEmission(new Color(0, 100, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(-123, -20, -5), 23d).setEmission(new Color(34, 139, 34)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(-76, -19, -5), 14d).setEmission(new Color(0, 128, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(-31, -25, -5), 17d).setEmission(new Color(107, 142, 35)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(11, -20, -5), 15d).setEmission(new Color(0, 100, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(49, -16, -5), 16d).setEmission(new Color(34, 139, 34)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(85, -25, -5), 18d).setEmission(new Color(0, 128, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(116, -20, -5), 13d).setEmission(new Color(107, 142, 35)).setMaterial(bushM2));
      scene._geometries.add(new Sphere(new Point(153, -22, -5), 19d).setEmission(new Color(0, 100, 0)).setMaterial(bushM1));
      scene._geometries.add(new Sphere(new Point(240, -24, -5), 20d).setEmission(new Color(34, 139, 34)).setMaterial(bushM2));

      //------------Light source------------

      scene.lights.add(new DirectionalLight(new Color(200, 200, 0), new Vector(0, 0, -1)));
      scene.lights.add(new SpotLight(new Color(123, 104, 238), new Point(10, -30, 40), new Vector(-1, 3, 7)));

      //-------------End light source--------------------

      // Adding houses
      for (int i = -3; i <= 0; i++) {
         double xOffset = i * 100;

         // Base of the house (rectangular prism approximation)
         // Using two triangles to form each face of the rectangular base
         scene._geometries.add(new Triangle(new Point(225 + xOffset, -25, -25), new Point(275 + xOffset, -25, -25), new Point(275 + xOffset, 25, -25))
                 .setEmission(new Color(139, 69, 19))  // Brown color for the cabin walls
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         scene._geometries.add(new Triangle(new Point(225 + xOffset, -25, -25), new Point(225 + xOffset, 25, -25), new Point(275 + xOffset, 25, -25))
                 .setEmission(new Color(139, 69, 19))  // Brown color for the cabin walls
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));

         // Left wall of the house
         scene._geometries.add(new Triangle(new Point(225 + xOffset, -25, -25), new Point(225 + xOffset, -25, 25), new Point(275 + xOffset, -25, 25))
                 .setEmission(new Color(160, 82, 45))  // Light Brown color for the cabin walls
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         scene._geometries.add(new Triangle(new Point(225 + xOffset, -25, -25), new Point(275 + xOffset, -25, 25), new Point(275 + xOffset, -25, -25))
                 .setEmission(new Color(160, 82, 45))  // Light Brown color for the cabin walls
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));

         // Right wall of the house
         scene._geometries.add(new Triangle(new Point(225 + xOffset, 25, -25), new Point(225 + xOffset, 25, 25), new Point(275 + xOffset, 25, 25))
                 .setEmission(new Color(160, 82, 45))  // Light Brown color for the cabin walls
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         scene._geometries.add(new Triangle(new Point(225 + xOffset, 25, -25), new Point(275 + xOffset, 25, 25), new Point(275 + xOffset, 25, -25))
                 .setEmission(new Color(160, 82, 45))  // Light Brown color for the cabin walls
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));

         // Roof of the house (triangular prism approximation)
         scene._geometries.add(new Triangle(new Point(220 + xOffset, -25, 25), new Point(250 + xOffset, 0, 50), new Point(280 + xOffset, -25, 25))
                 .setEmission(new Color(205, 92, 92))  // Reddish color for the roof
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         scene._geometries.add(new Triangle(new Point(220 + xOffset, 25, 25), new Point(250 + xOffset, 0, 50), new Point(280 + xOffset, 25, 25))
                 .setEmission(new Color(205, 92, 92))  // Reddish color for the roof
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));

         // Front roof face
         scene._geometries.add(new Triangle(new Point(220 + xOffset, -25, 25), new Point(220 + xOffset, 25, 25), new Point(250 + xOffset, 0, 50))
                 .setEmission(new Color(205, 92, 92))  // Reddish color for the roof
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         // Back roof face
         scene._geometries.add(new Triangle(new Point(280 + xOffset, -25, 25), new Point(280 + xOffset, 25, 25), new Point(250 + xOffset, 0, 50))
                 .setEmission(new Color(205, 92, 92))  // Reddish color for the roof
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
      }

      ImageWriter imageWriter = new ImageWriter("test_earth", 1400, 1400);
      camera.setRayTracer(new SimpleRayTracer(scene))
              .setImageWriter(imageWriter)
              .build()
              .renderImage()
              .writeToImage();
   }
}
