package primitives;

/**
 * Represents the material properties of a geometry.
 * Material includes diffuse coefficient (kD), specular coefficient (kS), and shininess (nShininess).
 */
public class Material {

    public Double3 kD = Double3.ZERO; // Diffuse coefficient
    public Double3 kS = Double3.ZERO; // Specular coefficient
    public Double3 kT = Double3.ZERO;
    public Double3 kR = Double3.ZERO;
    public int nShininess = 0; // Shininess factor

    /**
     * Sets the diffuse coefficient (kD) of the material.
     *
     * @param kD the diffuse coefficient as a {@link Double3} vector
     * @return the material itself (for chaining)
     */
    public Material setkD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the specular coefficient (kS) of the material.
     *
     * @param kS the specular coefficient as a {@link Double3} vector
     * @return the material itself (for chaining)
     */
    public Material setkS(Double3 kS) {
        this.kS = kS;
        return this;
    }
    public Material setkR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    public Material setkT(Double3 kT) {
        this.kT = kT;
        return this;
    }
    /**
     * Sets the diffuse coefficient (kD) of the material.
     *
     * @param kD the diffuse coefficient as a double value
     * @return the material itself (for chaining)
     */
    public Material setkD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular coefficient (kS) of the material.
     *
     * @param kS the specular coefficient as a double value
     * @return the material itself (for chaining)
     */
    public Material setkS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }
    public Material setkR(double kR) {
        this.kR = new Double3(kR);
        return this;
    }
    public Material setkT(double kT) {
        this.kT = new Double3(kT);
        return this;
    }
    /**
     * Sets the field KR
     *
     * @param KR parameter for KR
     * @return Material object
     */
    public Material setShininesR(double KR) {
        this.kR = new Double3(KR);
        return this;
    }

    /**
     * Sets the shininess factor (nShininess) of the material.
     *
     * @param nShininess the shininess factor as an integer
     * @return the material itself (for chaining)
     */
    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
