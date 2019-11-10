package cells;

import base.Enviro;
import critters.Critter;

public class Tree extends Living implements Food {
    protected double height;
    protected boolean deciduous = true;
    protected int baseH;

    public Tree(String type, Enviro enviro, int baseH, boolean deciduous) {
        super (type, enviro, 200);
        this.deciduous = deciduous;
        this.baseH = baseH;
        this.height = Math.abs (baseH + (r.nextGaussian () * baseH / 4)); //DA CONTROLLARE VALORI FINALI CHE POSSONO ESSERE NETIVI
        this.foodTypes.add ("Leafage");
        this.foodAmounts.add (Math.abs (baseH + (r.nextGaussian () * baseH / 4)));
    }

    @Override
    public void tick() {
        super.tick ();
        if (alive) {
            this.height = this.height + (enviro.getHumidity () / (baseH * 100)) * Math.abs (r.nextGaussian () / (baseH * 5));
        }
    }

    public void onEat(Critter critter, int index) {
        //TODO
    }

    public void onPassage(Critter critter) {
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
