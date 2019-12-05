package baseCells;

import base.Critter;
import base.Enviro;
import base.Foods;

import java.util.ArrayList;

public abstract class Tree extends Living implements Food {
    protected double height;
    protected boolean deciduous = true;
    protected int baseH;
    protected Foods foods;

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
        foods.grow ();
        this.height = this.height + (enviro.getHumidity () / (baseH * 100));
    }

    @Override
    public double onEat(Critter critter, int index) {
        return foods.eatFood (index);
    }
    @Override
    public ArrayList<Integer> getFoodTypes() {
        return foods.getFoodTypes ();
    }

    @Override
    public Double getFoodAmount(int index) {
        return foods.getFoodAmount (index);
    }

    public void onPassage(Critter critter) {
        critter.setSpeed (critter.getBaseSpeed () * 0.8);
    }

    @Override
    public void init() {
        foods.init ();
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
