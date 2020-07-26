package baseCells;

import base.Critter;
import base.Enviro;
import base.Foods;

public abstract class Tree extends Living {
    protected double height;
    protected boolean deciduous = true;
    protected int baseH;

    public Tree(String type, Enviro enviro, double growthRate, boolean deciduous) {
        super (type, enviro, 200);
        this.deciduous = deciduous;
        this.baseH = baseH;
        this.height = Math.abs (baseH + (r.nextGaussian () * baseH / 4));
        foods = new Foods (enviro);
        foods.addFood (growthRate, 2, 0, growthRate * enviro.getHumidity ());
    }

    @Override
    public void tick() {
        super.tick ();
        this.height = this.height + (enviro.getHumidity () / (baseH * 100));
    }

    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.8);
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isDeciduous() {
        return deciduous;
    }

    public void setDeciduous(boolean deciduous) {
        this.deciduous = deciduous;
    }
}
