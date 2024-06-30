package lighting;
import primitives.*;

import static java.lang.Math.max;
import static primitives.Util.alignZero;

public class SpotLight extends PointLight{
    private Vector direction;

    //private double narrowBeam = 1;
    public SpotLight(Color I, Point position, Vector direction) {
        super(I, position);
        this.direction = direction.normalize();
    }

    public SpotLight setKC(double kC) {

        super.setkC(kC);
        return this;
    }

    public SpotLight setKL(double kL) {
        super.setkL(kL);
        return this;
    }

    public SpotLight setKQ(double kQ) {
        super.setkQ(kQ);
        return this;
    }
//    public SpotLight(Color I, Point position, Vector direction,double kC, double kL, double kQ) {
//        super(I, position);
//        setkC(kC);
//        setkL(kL);
//        setkQ(kQ);
//        this.direction = direction.normalize();
//    }

//    public Color getIntensity(Point p) {
//        Vector l = getL(p);
//        if(l== null){
//            l= direction;
//        }
//        double cos = alignZero(direction.dotProduct(l)) ;
//        if (cos <= 0) {
//            return Color.BLACK;
//        }
//        return super.getIntensity(p).scale(cos);
//    }

    public Color getIntensity (Point p){
        return super.getIntensity(p).scale(Double.max(0, direction.dotProduct(getL(p))));
    }



    public Vector getL(Point p) {
        return super.getL(p);
    }
}
