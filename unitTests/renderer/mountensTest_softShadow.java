package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import static java.awt.Color.YELLOW;

public class mountensTest_softShadow {

	@Test
	public void ourPicture(){

		Scene scene = new Scene("Test scene")
		//		.setAmbientLight(new AmbientLight(new Color(255, 191, 191), Double3.ONE))//ADDED
				.setBackground(new Color( 191, 41, 99));

		 Camera.Builder camera     = Camera.getBuilder()

				.setRayTracer(new SimpleRayTracer(scene))
				.setLocation(new Point(10000, -30000, 10000)).setVpDistance(10000)
				.setDirection(new Vector(-1,3,-1).normalize(), new Vector(-1,3,10))
				 .setVpSize(150, 150);
		 ///חצי הפוך
//		Scene scene = new Scene("Test scene")
//		//		.setAmbientLight(new AmbientLight(new Color(255, 191, 191), Double3.ONE))//ADDED
//				.setBackground(new Color( 191, 41, 99));
//
//		 Camera.Builder camera     = Camera.getBuilder()
//
//				.setRayTracer(new SimpleRayTracer(scene))
//				.setLocation(new Point(10000, -30000, 10000)).setVpDistance(10000)
//				.setDirection(Point.ZERO, Vector.Y)
//				.setVpSize(150, 150);

		Material mountainM = new Material().setkD(0.6).setkS(0.4).setnShininess(200),
				snowM = new Material().setkD(0.3).setkS(0.6).setnShininess(200).setShininesR(0.4),
				seaM = new Material().setkD(0.2).setkS(0.9).setnShininess(3000).setShininesR(0.6),
				birdM = new Material().setkD(0.2).setkS(0.6).setnShininess(300),
				cloudM = new Material().setkD(0.5).setkS(0.6).setnShininess(3000),
				bushM = new Material().setkD(0.2).setkS(0.1).setnShininess(300);

		//sun
		Material sunM = new Material().setkD(0.2).setkS(0.2).setnShininess(200).setkT(0.6);
		scene._geometries.add(new Sphere(new Point(-100, 140, 100), 40d).setEmission(new Color(251, 58, 16)).setMaterial(sunM));

		//--------------mountains--------------------
		// Triangle 1
		scene._geometries.add(new Triangle(new Point(-120,10,0), new Point(-200, -45, 0), //
				new Point(-200, 0, 80)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-120,10,0), new Point(-200, 45, 0), //
				new Point(-200, 0, 80)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-260,10,0), new Point(-200, -45, 0), //
				new Point(-200, 0, 80)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-260,10,0), new Point(-200, 45, 0), //
				new Point(-200, 0, 80)).setEmission(new Color(117,68,35)).setMaterial(mountainM));

		// Triangle 2
		scene._geometries.add(new Triangle(new Point(-75,0,100), new Point(-75, -40, 0), //
				new Point(-150, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-75,0,100), new Point(-75, -40, 0), //
				new Point(0, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-75,0,100), new Point(-75, 30,0), //
				new Point(-150, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-75,0,100), new Point(-75, 30, 0), //
				new Point(0, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));

		// Triangle 3
		scene._geometries.add(new Triangle(new Point(0,0,90), new Point(0, -40, 0), //
				new Point(-50, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(0,0,90), new Point(0, -40, 0), //
				new Point(50, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(0,0,90), new Point(0, 30,0), //
				new Point(-50, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(0,0,90), new Point(0, 30, 0), //
				new Point(50, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));

		// Triangle 4
		scene._geometries.add(new Triangle(new Point(75,0,100), new Point(75, -40, 0), //
				new Point(150, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(75,0,100), new Point(75, -40, 0), //
				new Point(0, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(75,0,100), new Point(75, 30,0), //
				new Point(150, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(75,0,100), new Point(75, 30, 0), //
				new Point(0, 0, 0)).setEmission(new Color(117,68,35)).setMaterial(mountainM));

		// Triangle 5
		scene._geometries.add(new Triangle(new Point(120,10,0), new Point(200, 45, 0), //
				new Point(200, 0, 80)).setEmission(new  Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(120,10,0), new Point(200, -45, 0), //
				new Point(200, 0, 80)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(260,10,0), new Point(200, -45, 0), //
				new Point(200, 0, 80)).setEmission(new Color(117,68,35)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(260,10,0), new Point(200, 40, 0), //
				new Point(200, 0, 80)).setEmission(new Color(90,39,41)).setMaterial(mountainM));

		// Triangle 6
		scene._geometries.add(new Triangle(new Point(-150,80,160), new Point(-160, 130, 0), //
				new Point(-250, 70, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-150,80,160), new Point(-160, 130, 0), //
				new Point(-30, 60, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-150,80,160), new Point(-130, 20, 0), //
				new Point(-250, 70, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-150,80,160), new Point(-130, 20, 0), //
				new Point(-30, 60, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));

		// Snow 6
		scene._geometries.add(new Triangle(new Point(-108,73.12,105), new Point(-153.75,99,100), //
				new Point(-150,80,160)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(-108,73.12,105), new Point(-142.5,57,100), //
				new Point(-150,80,160)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(-185,76.56,105), new Point(-153.75,99,100), //
				new Point(-150,80,160)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(-185,76.56,105), new Point(-142.5,57,100), //
				new Point(-150,80,160)).setEmission(new Color(250,245,245)).setMaterial(snowM));

		// Triangle 7
		scene._geometries.add(new Triangle(new Point(-50,50,120), new Point(-50, 70, 0), //
				new Point(-120, 40, 0)).setEmission(new Color(101,67,33)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-50,50,120), new Point(-50, 70, 0), //
				new Point(0, 40, 0)).setEmission(new Color(101,67,33)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-50,50,120), new Point(-40, 20,0), //
				new Point(-120, 40, 0)).setEmission(new Color(101,67,33)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(-50,50,120), new Point(-40, 20, 0), //
				new Point(0, 40, 0)).setEmission(new Color(101,67,33)).setMaterial(mountainM));
		// Snow 7
		scene._geometries.add(new Triangle(new Point(-39,47.92,95), new Point(-47.5,42,90), //
				new Point(-50,50,120)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(-39,47.92,95), new Point(-50,55.5,90), //
				new Point(-50,50,120)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(-65,47.92,95), new Point(-47.5,42,90), //
				new Point(-50,50,120)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(-65,47,95), new Point(-50,55.5,90), //
				new Point(-50,50,120)).setEmission(new Color(250,245,245)).setMaterial(snowM));

		// Triangle 8
		scene._geometries.add(new Triangle(new Point(50,50,120), new Point(40, 10, 0), //
				new Point(100, 50, 0)).setEmission(new Color(101,67,33)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(50,50,120), new Point(40, 10, 0), //
				new Point(0, 50, 0)).setEmission(new Color(101,67,33)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(50,50,120), new Point(40, 70,0), //
				new Point(100, 50, 0)).setEmission(new Color(101,67,33)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(50,50,120), new Point(40, 70, 0), //
				new Point(0, 50, 0)).setEmission(new Color(101,67,33)).setMaterial(mountainM));
		// Snow 8
		scene._geometries.add(new Triangle(new Point(61,50,95), new Point(47.5,55.5,90), //
				new Point(50,50,120)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(61,50,95), new Point(47.5,39.5,90), //
				new Point(50,50,120)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(39,50,95), new Point(47.5,55.5,90), //
				new Point(50,50,120)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(39,50,95), new Point(47.5,39.5,90), //
				new Point(50,50,120)).setEmission(new Color(250,245,245)).setMaterial(snowM));

		// Triangle 9
		scene._geometries.add(new Triangle(new Point(150,80,160), new Point(160, 130, 0), //
				new Point(250, 70, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(150,80,160), new Point(160, 130, 0), //
				new Point(30, 60, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(150,80,160), new Point(130, 20, 0), //
				new Point(250, 70, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(150,80,160), new Point(130, 20, 0), //
				new Point(30, 60, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		// Snow 9
		scene._geometries.add(new Triangle(new Point(185,76.56,105), new Point(153.75,99,100), //
				new Point(150, 80, 160)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(185,76.56,105), new Point(142.5,57,100), //
				new Point(150, 80, 160)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(108,73.12,105), new Point(153.75,99,100), //
				new Point(150, 80, 160)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(108,73.12,105), new Point(142.5,57,100), //
				new Point(150, 80, 160)).setEmission(new Color(250,245,245)).setMaterial(snowM));

		// Triangle 10
		scene._geometries.add(new Triangle(new Point(0,80,160), new Point(0, 100, 0), //
				new Point(90, 80, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(0,80,160), new Point(0, 100, 0), //
				new Point(-90, 80, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(0,80,160), new Point(0, 50,0), //
				new Point(90, 80, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(0,80,160), new Point(0, 50, 0), //
				new Point(-90, 80, 0)).setEmission(new Color(92,72,39)).setMaterial(mountainM));
		// Snow 10
		scene._geometries.add(new Triangle(new Point(-29,80,110), new Point(0,88,100), //
				new Point(0,80,160)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(-29,80,110), new Point(0,68,100), //
				new Point(0,80,160)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(29,80,110), new Point(0,88,100), //
				new Point(0,80,160)).setEmission(new Color(250,245,245)).setMaterial(snowM));
		scene._geometries.add(new Triangle(new Point(29,80,110), new Point(0,68,100), //
				new Point(0,80,160)).setEmission(new Color(250,245,245)).setMaterial(snowM));

		// Triangle 11
		scene._geometries.add(new Triangle(new Point(0,150,100), new Point(0, 200, 0), //
				new Point(160, 130, 0)).setEmission(new Color(76,65,40)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(0,150,100), new Point(0, 200, 0), //
				new Point(-160, 130, 0)).setEmission(new Color(76,65,40)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(0,150,100), new Point(0, 100, 0), //
				new Point(160, 130, 0)).setEmission(new Color(76,65,40)).setMaterial(mountainM));
		scene._geometries.add(new Triangle(new Point(0,150,100), new Point(0, 100, 0), //
				new Point(-160, 130, 0)).setEmission(new Color(76,65,40)).setMaterial(mountainM));

		//-----------End mountains-------------------

		// Earth
		scene._geometries.add(new Triangle(new Point(0,140,0), new Point(-600, -45, 0), //
				new Point(600, -45, 0)).setEmission(new Color(92,73,57)));

		// Sea
		scene._geometries.add(new Triangle(new Point(10000,0,0), new Point(-10000, 0, 0), //
				new Point(0, -10000, 0)).setEmission(new Color(0,25,50)).setMaterial(seaM));

		// Sky
		scene._geometries.add(new Triangle(new Point(10000,10000,0), new Point(-10000, 10000, 0), //
				new Point(0, 10000, 100000)).setEmission(new Color(191, 41, 99)));

		// ---------------Clouds---------------
		Color cloudColor = new Color(255, 178, 200);

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


		//---------------Birds------------------
		// Bird 1
		scene._geometries.add(new Triangle(new Point(-66,-199,39), new Point(-66,-184,46), //
				new Point(-61,-184,43)).setEmission(new Color(0,0,0)).setMaterial(birdM));
		scene._geometries.add(new Triangle(new Point(-66,-199,39), new Point(-83,-198,43), //
				new Point(-76,-209,43)).setEmission(new Color(0,0,0)).setMaterial(birdM));
		scene._geometries.add(new Triangle(new Point(-40,-173,39), new Point(-66,-184,46), //
				new Point(-61,-184,43)).setEmission(new Color(0,0,0)).setMaterial(birdM));
		scene._geometries.add(new Triangle(new Point(-83,-220,37), new Point(-83,-198,43), //
				new Point(-76,-209,43)).setEmission(new Color(0,0,0)).setMaterial(birdM));
		// Bird 2
		scene._geometries.add(new Triangle(new Point(162,59,133), new Point(168,63,139), //
				new Point(169,55,138)).setEmission(new Color(0,0,0)).setMaterial(birdM));
		scene._geometries.add(new Triangle(new Point(162,59,133), new Point(154,65,140), //
				new Point(155,57,140)).setEmission(new Color(0,0,0)).setMaterial(birdM));
		scene._geometries.add(new Triangle(new Point(177,59,130), new Point(168,63,139), //
				new Point(169,55,138)).setEmission(new Color(0,0,0)).setMaterial(birdM));
		scene._geometries.add(new Triangle(new Point(147,60,136), new Point(154,65,140), //
				new Point(155,57,140)).setEmission(new Color(0,0,0)).setMaterial(birdM));
		//------------End birds--------------

		// Bushes
		scene._geometries.add(new Sphere(new Point(225, -25, -5),22d).setEmission(new Color(114,140,0)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(172, -20, -5),15d).setEmission(new Color(108,187,60)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(130, -15, -5), 24d).setEmission(new Color(37,65,23)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(160, -20, -5), 10d).setEmission(new Color(114,140,0)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(100, -20, -5), 23d).setEmission(new Color(108,187,60)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(30, -19, -5), 14d).setEmission(new Color(56,124,68)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-24, -25, -5), 17d).setEmission(new Color(133,187,101)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-50, -20, -5), 15d).setEmission(new Color(114,140,0)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-100, -16, -5), 23d).setEmission(new Color(108,187,60)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-130, -25, -5), 18d).setEmission(new Color(37,65,23)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-165, -20, -5), 23d).setEmission(new Color(56,124,68)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-257, -22, -5), 19d).setEmission(new Color(114,140,0)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-233, -24, -5), 20d).setEmission(new Color(108,187,60)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-246, -25, -5),22d).setEmission(new Color(114,140,0)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-220, -20, -5),15d).setEmission(new Color(108,187,60)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-182, -15, -5), 21d).setEmission(new Color(37,65,23)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-147, -20, -5), 10d).setEmission(new Color(114,140,0)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-123, -20, -5), 23d).setEmission(new Color(108,187,60)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-76, -19, -5), 14d).setEmission(new Color(56,124,68)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(-31, -25, -5), 17d).setEmission(new Color(133,187,101)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(11, -20, -5), 15d).setEmission(new Color(114,140,0)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(49, -16, -5), 16d).setEmission(new Color(108,187,60)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(85, -25, -5), 18d).setEmission(new Color(37,65,23)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(116, -20, -5), 13d).setEmission(new Color(56,124,68)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(153, -22, -5), 19d).setEmission(new Color(114,140,0)).setMaterial(bushM));
		scene._geometries.add(new Sphere(new Point(240, -24, -5), 20d).setEmission(new Color(108,187,60)).setMaterial(bushM));

		//------------Light source------------

		scene.lights.add(new DirectionalLight(new Color(200,200,0), new Vector(0,0,-1)));
		scene.lights.add(new SpotLight(new Color(123,104,238),new Point(10,-30,40), new Vector(-1, 3,7)));

		// Sun
		scene.lights.add(new PointLight(new Color(YELLOW),new Point(-100, 140, 100)).setkC(1));

		//-------------End light source--------------------


		ImageWriter imageWriter = new ImageWriter("mountensTest", 1400, 1400);
		camera.setRayTracer(new SimpleRayTracer(scene))
				.setImageWriter(imageWriter)
				.build()
				.renderImage()
				.writeToImage();
	}
}