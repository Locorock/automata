package base;

import enumLists.EventList;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class Time extends Thread {
    ArrayList<Event> events;
    private Random r;
    private double tickSize; //in seconds
    private int cycleSize; //in ticks
    private int ticks = 0;
    private World w;
    private int seed;

    public Time(double tickSize, int cycleSize, World w) {
        this.tickSize = tickSize;
        this.cycleSize = cycleSize;
        this.w = w;
        this.events = new ArrayList<Event> ();
        this.r = w.getR ();
    }

    @Override
    public void run() {
        while (isAlive () && !isInterrupted ()) {
            try {
                double elapsed = 0;
                long start = System.nanoTime ();
                tick ();
                ticks++;
                if (ticks >= cycleSize) {
                    cycle ();
                    ticks = 0;
                }
                elapsed = (double) (System.nanoTime () - start) / 1000000;
                System.out.println ("Ceeclo che è durato " + elapsed);
                if (elapsed < tickSize)
                    this.sleep ((long) tickSize - (long) elapsed);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }
    }

    public void tick() {
        //DA IMPLEMENTARE CON CRITTERS
    }

    public void cycle() {
        generateEvents ();
        cycleEvents ();
        cycleWorld ();
        w.panel.repaint ();
        w.panel2.repaint ();
    }

    public void generateEvents() {
        long start = System.nanoTime ();
        for (EventList event : EventList.values ()) {
            for (Enviro enviro : w.getEnviros ()) {
                if (event.getBiomes () == null || event.getBiomes ().contains (enviro.getBiome ())) {
                    int randomN = r.nextInt (event.getRarity ());
                    if (event.getHumAsc () == 1) {
                        int prob = (int) Math.floor ((event.getRarity () * 10) / (enviro.getAvgHum () / 4 + 5));
                        randomN = r.nextInt (prob);
                    }
                    if (randomN == 0) {
                        try {
                            Event e = (Event) Class.forName ("events." + event.name ()).getDeclaredConstructor (new Class[]{Enviro.class, String.class}).newInstance (enviro, event.name ());
                            events.add (e);
                            System.out.println ("Aggiunto un evento di tipo: " + event.name () + ", size: " + e.size);
                        } catch (InstantiationException ex) {
                            ex.printStackTrace ();
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace ();
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace ();
                        } catch (NoSuchMethodException ex) {
                            ex.printStackTrace ();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace ();
                        }
                    }
                }
            }
        }
        System.out.println ("Ciclo generazione: " + (double) (System.nanoTime () - start) / 1000000);
    }

    public void cycleEvents() {
        long start = System.nanoTime ();
        for (int i = 0; i < events.size (); i++) {
            Event event = events.get (i);
            event.update ();
            if (event.getDuration () <= 0) {
                events.remove (event);
                i--;
            }
        }
        System.out.println ("Ciclo eventi: " + (double) (System.nanoTime () - start) / 1000000);
    }

    public void cycleWorld() {
        long start = System.nanoTime ();
        for (Enviro e : w.getEnviros ()) {
            cycleEnviro (e);
        }
        System.out.println ("Ciclo celle: " + (double) (System.nanoTime () - start) / 1000000);
    }

    public void cycleEnviro(Enviro e) {
        //e.setTemperature (e.getTemperature()+(e.getAvgTemp()-e.getTemperature ())/100+r.nextGaussian());
        //e.setHumidity (e.getHumidity ()+(e.getAvgHum()-e.getHumidity ())/50+r.nextGaussian());
        e.setTemperature (e.getTemperature () + (e.getAvgTemp () - e.getTemperature ()) / 10);
        if (e.isRiver ()) {
            e.setHumidity (e.getHumidity () + (e.getAvgHum () + 15 - e.getHumidity ()) / 10);
        } else {
            e.setHumidity (e.getHumidity () + (e.getAvgHum () - e.getHumidity ()) / 10);
        }
        for (int i = 0; i < e.getWidth (); i++) {
            for (int j = 0; j < e.getWidth (); j++) {
                e.getGrid ()[j][i].tick ();
            }
        }
    }
}
