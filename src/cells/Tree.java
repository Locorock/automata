package cells;

import base.Cell;
import base.Enviro;

import java.util.Random;

public class Tree extends Cell {
    protected double height;
    protected boolean deciduous = true;
    protected int timeToLive; //Spooky stuff
    protected boolean dead = false;
    protected Random r;

    public Tree(String type, Enviro enviro, String[] args) {
        super (type, enviro);
        this.r = enviro.getR ();
        generateStats ();
    }

    public void generateStats() {
        double baseH = 2;
        if (enviro.getBiome () == "savanna" || enviro.getBiome () == "taiga") {
            baseH = 4;
        }
        if (enviro.getBiome () == "taiga") {
            deciduous = false;
        }
        this.height = baseH + (r.nextGaussian () * 2) % baseH;
        this.timeToLive = (int) Math.abs (200 + r.nextGaussian () * 100);
    }

    public void tick() {
        if (!dead) {
            timeToLive--;
            if (timeToLive <= 0) {
                this.dead = true;
            }
            this.height = this.height + (enviro.getHumidity () / 200) * Math.abs (r.nextGaussian () / 10);
        }
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

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
