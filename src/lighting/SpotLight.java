package lighting;
import primitives.*;

import static primitives.Util.alignZero;

public class SpotLight extends PointLight{
    private Vector direction;

    public SpotLight(Color I, Point position, Vector direction,double kC, double kL, double kQ) {
        super(I, position);
        setkC(kC);
        setkL(kL);
        setkQ(kQ);
        this.direction = direction.normalize();
    }

    public Color getIntensity(Point p) {
        Vector l = getL(p);
        if(l== null){
            l= direction;
        }
        double cos = alignZero(direction.dotProduct(l)) ;
        if (cos <= 0) {
            return Color.BLACK;
        }
        return super.getIntensity(p).scale(cos);
    }

    public Vector getL(Point p) {
        return super.getL(p);
    }
}
