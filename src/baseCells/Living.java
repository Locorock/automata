package baseCells;

import base.Cell;
import base.Enviro;

public abstract class Living extends Cell implements Solid {
    protected boolean alive;
    protected int timeToLive;
    protected int averageTTL;

    public Living(String type, Enviro enviro, int averageTTL) { //200
        super (type, enviro);
        this.averageTTL = averageTTL;
        if (averageTTL != -1) {
            this.timeToLive = (int) Math.abs (averageTTL + (r.nextGaussian () * (averageTTL / 2)));
        } else {
            //this.timeToLive = -1; no dying for you now
        }
        this.alive = true;
    }

    public void tick() {
        super.tick ();
        if (false) { //timeToLive != -1 THERE'S NO WAY FOR TREES SO SPREAD SO FOR NOW JUST MAKE THEM IMMORTAL
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
