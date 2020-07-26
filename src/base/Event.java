package base;

import enumLists.EventList;

import java.util.ArrayList;
import java.util.Random;

public abstract class Event {
    public int size;
    protected Enviro epicenter;
    protected int duration;
    protected int seed;
    protected String name;
    protected Random r;
    protected ArrayList<Enviro>[] affected;

    public Event(Enviro epicenter, String name) {
        this.epicenter = epicenter;
        this.seed = seed;
        this.name = name;
        this.r = epicenter.getR ();
        generateEvent ();
    }

    public void update() {
        for (int i = 0; i < affected.length; i++) {
            for (int j = 0; j < affected[i].size (); j++) {
                if (duration == 0) {
                    remove (affected.length - i, affected[i].get (j));
                } else {
                    apply (affected.length - i, affected[i].get (j));
                }
            }
        }
        duration--;
    }

    public abstract void apply(int strength, Enviro e);

    public abstract void remove(int strength, Enviro e);

    private void generateEvent() {
        EventList eventType = EventList.valueOf (name);
        this.size = (int) Math.round (eventType.getMeanSize () + r.nextGaussian () * eventType.getStdSize ());
        if (size < 1) {
            size = 1;
        }
        this.duration = (int) Math.round (eventType.getMeanDuration () + r.nextGaussian () * eventType.getStdDuration ());
        if (duration < 1) {
            duration = 1;
        }

        this.affected = epicenter.scanNeighbours (size);
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Enviro>[] getAffected() {
        return affected;
    }

    public void setAffected(ArrayList<Enviro>[] affected) {
        this.affected = affected;
    }

    public Enviro getEpicenter() {
        return epicenter;
    }

    public void setEpicenter(Enviro epicenter) {
        this.epicenter = epicenter;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
