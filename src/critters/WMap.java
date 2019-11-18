package critters;

import base.Enviro;

public class WMap {
    Critter c;
    double[][] weights;
    Enviro e;

    public WMap(Enviro e, Critter c) {
        this.e = e;
        this.c = c;
        this.weights = new double[e.getWidth ()][e.getWidth ()];
        fill ();
    }

    public void fill() {
        for (int i = 0; i < e.getWidth (); i++) {
            for (int j = 0; j < e.getWidth (); j++) {
                weights[j][i] = c.calcWeight (e.getGrid ()[j][i]);
            }
        }
    }

    public double getWeight(int x, int y) {
        return this.weights[x][y];
    }

    public Critter getC() {
        return c;
    }

    public void setC(Critter c) {
        this.c = c;
    }

    public double[][] getWeights() {
        return weights;
    }

    public void setWeights(double[][] weights) {
        this.weights = weights;
    }

    public Enviro getE() {
        return e;
    }

    public void setE(Enviro e) {
        this.e = e;
    }
}
