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

public class finalSoft {

   @Test
   public void ourPicture() {
      Scene scene = new Scene("Test scene")
              .setBackground(new Color(70, 70, 90))
              .setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.01));  // Add soft ambient light

      Camera.Builder camera = Camera.getBuilder()
              .setRayTracer(new SimpleRayTracer(scene).setSoftShadows(true))
              .setLocation(new Point(0, -20000, 20000))  // Set camera farther away
              .setVpDistance(10000)  // Increase virtual plane distance
              .setDirection(new Vector(0, 1, -1).normalize(), new Vector(0, 1, 0))  // Set camera direction towards the scene
              .setVpSize(500, 500);

      Material mountainM1 = new Material().setkD(0.6).setkS(0.4).setnShininess(200),
              mountainM2 = new Material().setkD(0.5).setkS(0.3).setnShininess(150),
              snowM = new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4),
              seaM = new Material().setkD(0.2).setkS(0.9).setnShininess(3000).setShininesR(0.6),
              cloudM = new Material().setkD(0.5).setkS(0.6).setnShininess(3000),
              bushM1 = new Material().setkD(0.2).setkS(0.1).setnShininess(300),
              bushM2 = new Material().setkD(0.3).setkS(0.2).setnShininess(200),
              baseM = new Material().setkD(0.7).setkS(0.3).setnShininess(100);

      // Sun
      Material sunM = new Material().setkD(0.2).setkS(0.2).setnShininess(200).setkT(0.6);
      scene._geometries.add(new Sphere(new Point(-100, 140, 400), 90d).setEmission(new Color(251, 58, 16)).setMaterial(sunM));

      // Snow
      for (int i = 0; i < 100; i++) {
         double x = Math.random() * 2000 - 1000;
         double y = Math.random() * 2000 - 1000;
         double z = Math.random() * 500;  // Limit snow height

         if (y > -90) {  // Ensure snow is above ground level
            double radius = Math.random() * 10 + 5;
            scene._geometries.add(new Sphere(new Point(x, y, z), radius)
                    .setEmission(new Color(255, 255, 255))
                    .setMaterial(new Material().setkD(0.2).setkS(0.8).setnShininess(100).setkT(0.3)));
         }
      }

      // Mountains
      // Triangle 1
      scene._geometries.add(new Triangle(
              new Point(-335, 125, 320),
              new Point(-355, 225, 0),
              new Point(-535, 105, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      scene._geometries.add(new Triangle(
              new Point(-335, 125, 320),
              new Point(-355, 225, 0),
              new Point(-95, 85, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      scene._geometries.add(new Triangle(
              new Point(-335, 125, 320),
              new Point(-295, 55, 0),
              new Point(-535, 105, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      scene._geometries.add(new Triangle(
              new Point(-335, 125, 320),
              new Point(-295, 55, 0),
              new Point(-95, 85, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      // Snow on Triangle 1
      scene._geometries.add(new Triangle(
              new Point(-298, 146.24, 210),
              new Point(-407.5, 198, 200),
              new Point(-335, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      scene._geometries.add(new Triangle(
              new Point(-298, 146.24, 210),
              new Point(-285, 114, 200),
              new Point(-335, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      scene._geometries.add(new Triangle(
              new Point(-370, 153.12, 210),
              new Point(-407.5, 198, 200),
              new Point(-335, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      scene._geometries.add(new Triangle(
              new Point(-370, 153.12, 210),
              new Point(-285, 114, 200),
              new Point(-335, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      // Triangle 2
      scene._geometries.add(new Triangle(
              new Point(265, 125, 320),
              new Point(285, 225, 0),
              new Point(465, 105, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      scene._geometries.add(new Triangle(
              new Point(265, 125, 320),
              new Point(285, 225, 0),
              new Point(45, 85, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      scene._geometries.add(new Triangle(
              new Point(265, 125, 320),
              new Point(225, 55, 0),
              new Point(465, 105, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      scene._geometries.add(new Triangle(
              new Point(265, 125, 320),
              new Point(225, 55, 0),
              new Point(45, 85, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      // Snow on Triangle 2
      scene._geometries.add(new Triangle(
              new Point(315, 153.12, 210),
              new Point(370, 198, 200),
              new Point(265, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      scene._geometries.add(new Triangle(
              new Point(315, 153.12, 210),
              new Point(270, 114, 200),
              new Point(265, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      scene._geometries.add(new Triangle(
              new Point(240, 146.24, 210),
              new Point(270, 198, 200),
              new Point(265, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      scene._geometries.add(new Triangle(
              new Point(240, 146.24, 210),
              new Point(240, 114, 200),
              new Point(265, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      // Triangle 3
      scene._geometries.add(new Triangle(
              new Point(-35, 125, 320),
              new Point(-35, 225, 0),
              new Point(145, 105, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      scene._geometries.add(new Triangle(
              new Point(-35, 125, 320),
              new Point(-35, 225, 0),
              new Point(-175, 85, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      scene._geometries.add(new Triangle(
              new Point(-35, 125, 320),
              new Point(-35, 55, 0),
              new Point(145, 105, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      scene._geometries.add(new Triangle(
              new Point(-35, 125, 320),
              new Point(-35, 55, 0),
              new Point(-175, 85, 0))
              .setEmission(new Color(92, 72, 39))
              .setMaterial(new Material().setkD(0.5).setkS(0.3).setnShininess(150)));

      // Snow on Triangle 3
      scene._geometries.add(new Triangle(
              new Point(-64, 160, 220),
              new Point(0, 175, 200),
              new Point(0, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      scene._geometries.add(new Triangle(
              new Point(-64, 160, 220),
              new Point(0, 136, 200),
              new Point(0, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      scene._geometries.add(new Triangle(
              new Point(64, 160, 220),
              new Point(0, 175, 200),
              new Point(0, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      scene._geometries.add(new Triangle(
              new Point(64, 160, 220),
              new Point(0, 136, 200),
              new Point(0, 125, 320))
              .setEmission(new Color(250, 245, 245))
              .setMaterial(new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4)));

      // Earth
      scene._geometries.add(new Triangle(new Point(0, 250, 25), new Point(-1200, -90, 0), //
              new Point(1200, -90, 0)).setEmission(new Color(92, 73, 57)).setMaterial
              (new Material().setkD(0.5).setkS(0.5).setnShininess(30)));  // Apply base material for the ground

      // Sea
      scene._geometries.add(new Triangle(new Point(10000, 0, 0), new Point(-10000, 0, 0), //
              new Point(0, -10000, 0)).setEmission(new Color(0, 25, 50)).setMaterial(new Material().setkD(0.2).setkS(0.9).setnShininess(3000).setShininesR(0.6)));

      // Sky
      scene._geometries.add(new Triangle(new Point(10000, 10000, 0), new Point(-10000, 10000, 0), //
              new Point(0, 10000, 100000)).setEmission(new Color(191, 41, 99)));

      // Clouds
      Color cloudColor = new Color(169, 169, 169);  // Change cloud color to a darker gray

      scene._geometries.add(new Sphere(new Point(365, 165, 415), 60d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(340, 165, 405), 50d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(390, 165, 405), 50d).setEmission(cloudColor).setMaterial(cloudM));

      scene._geometries.add(new Sphere(new Point(-315, 225, 335), 60d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(-290, 225, 325), 50d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(-340, 225, 325), 50d).setEmission(cloudColor).setMaterial(cloudM));

      scene._geometries.add(new Sphere(new Point(135, 165, 465), 60d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(110, 165, 455), 50d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(160, 165, 455), 50d).setEmission(cloudColor).setMaterial(cloudM));

      scene._geometries.add(new Sphere(new Point(-445, 5, 335), 60d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(-420, 5, 325), 50d).setEmission(cloudColor).setMaterial(cloudM));
      scene._geometries.add(new Sphere(new Point(-470, 5, 325), 50d).setEmission(cloudColor).setMaterial(cloudM));

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

      // Adding houses
      for (int i = -3; i <= 0; i++) {
         double xOffset = i * 100;

         scene._geometries.add(new Triangle(new Point(225 + xOffset, -25, -25), new Point(275 + xOffset, -25, -25), new Point(275 + xOffset, 25, -25))
                 .setEmission(new Color(139, 69, 19))  // Brown color for the cabin walls
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         scene._geometries.add(new Triangle(new Point(225 + xOffset, -25, -25), new Point(225 + xOffset, 25, -25), new Point(275 + xOffset, 25, -25))
                 .setEmission(new Color(139, 69, 19))
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));

         scene._geometries.add(new Triangle(new Point(225 + xOffset, -25, -25), new Point(225 + xOffset, -25, 25), new Point(275 + xOffset, -25, 25))
                 .setEmission(new Color(160, 82, 45))  // Light Brown color for the cabin walls
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         scene._geometries.add(new Triangle(new Point(225 + xOffset, -25, -25), new Point(275 + xOffset, -25, 25), new Point(275 + xOffset, -25, -25))
                 .setEmission(new Color(160, 82, 45))
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));

         scene._geometries.add(new Triangle(new Point(225 + xOffset, 25, -25), new Point(225 + xOffset, 25, 25), new Point(275 + xOffset, 25, 25))
                 .setEmission(new Color(160, 82, 45))  // Light Brown color for the cabin walls
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         scene._geometries.add(new Triangle(new Point(225 + xOffset, 25, -25), new Point(275 + xOffset, 25, 25), new Point(275 + xOffset, 25, -25))
                 .setEmission(new Color(160, 82, 45))
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));

         scene._geometries.add(new Triangle(new Point(220 + xOffset, -25, 25), new Point(250 + xOffset, 0, 50), new Point(280 + xOffset, -25, 25))
                 .setEmission(new Color(205, 92, 92))  //  color for the roof
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         scene._geometries.add(new Triangle(new Point(220 + xOffset, 25, 25), new Point(250 + xOffset, 0, 50), new Point(280 + xOffset, 25, 25))
                 .setEmission(new Color(205, 92, 92))
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));

         scene._geometries.add(new Triangle(new Point(220 + xOffset, -25, 25), new Point(220 + xOffset, 25, 25), new Point(250 + xOffset, 0, 50))
                 .setEmission(new Color(205, 92, 92))
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
         scene._geometries.add(new Triangle(new Point(280 + xOffset, -25, 25), new Point(280 + xOffset, 25, 25), new Point(250 + xOffset, 0, 50))
                 .setEmission(new Color(205, 92, 92))
                 .setMaterial(new Material().setkD(0.7).setkS(0.3).setnShininess(200)));
      }

      // Light sources
      scene.lights.add(new SpotLight(new Color(255, 255, 200),
              new Point(-150, 200, 400),
              new Vector(-0.5, -1, -1),20)
              .setkC(1)
              .setkL(0.00003)
              .setkQ(0.00001));

      scene.lights.add(new SpotLight(new Color(255, 223, 100),
              new Point(-150, 200, 400),
              new Vector(-1, -1, -2))
              .setkC(1)
              .setkL(0.00005)
              .setkQ(0.00002));


      scene.lights.add(new DirectionalLight(new Color(200, 200, 0), new Vector(0, 0, -1)));

      scene.lights.add(new DirectionalLight(new Color(150, 150, 255), new Vector(2, -3, -1)));

      ImageWriter imageWriter = new ImageWriter("finalSoft", 1000, 1000);
      camera.setRayTracer(new SimpleRayTracer(scene))
              .setImageWriter(imageWriter)
              .build()
              .renderImage()
              .writeToImage();
   }

}
