package critters;

import base.Enviro;
import base.World;

public class WMap {
    Critter c;
    double[][] weights;
    World w;

    public WMap(Critter c, World w) {
        this.c = c;
        this.w = w;
        this.weights = new double[Enviro.width * w.getMap ().get (0).size ()][Enviro.width * w.getMap ().size ()];
        fill ();
    }

    public void fill() {
        for (int i = 0; i < w.getMap ().size (); i++) {
            for (int j = 0; j < w.getMap ().get (i).size (); j++) {
                Enviro e = w.getMap ().get (i).get (j);
                if (e != null) {
                    int width = e.getWidth ();
                    for (int k = 0; k < width; k++) {
                        for (int l = 0; l < width; l++) {
                            weights[width * j + l][width * i + k] = c.calcWeight (e.getGrid ()[l][k]);
                        }
                    }
                }
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
}
