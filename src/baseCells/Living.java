package baseCells;

import base.Cell;
import base.Enviro;

public abstract class Living extends Cell implements Solid {
    protected boolean alive;
    protected int timeToLive;
    protected int averageTTL;

    public Living(String type, Enviro enviro, double speedMod, int averageTTL) { //200
        super (type, enviro, speedMod);
        this.averageTTL = averageTTL;
        if (averageTTL != -1) {
            this.timeToLive = (int) Math.abs (averageTTL + (r.nextGaussian () * (averageTTL / 2)));
        } else {
            this.timeToLive = -1;
        }
        this.alive = true;
    }

    public void tick() {
        if (timeToLive != -1) {
            if (isAlive ()) {
                timeToLive--;
                if (timeToLive <= 0) {
                    this.alive = false;
                }
            } else {
                //decompose TODO
            }
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

}
