package base;

import enumLists.EventList;
import graphics.MainGUI;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class Time extends Thread {
    ArrayList<Event> events;
    public static int count = 0;
    private final Random r;
    private final double tickSize; //in seconds
    private int ticks = 0;
    private final int cycleSize; //in ticks
    private final int tot = 0;
    private int seed;
    private final World w;
    private final MainGUI gui;
    public double lastPop = 0;
    public boolean running = false;
    public boolean loop = false;
    public static HashMap<String, Double> times;
    public static double[] totals;

    public Time(double tickSize, int cycleSize, World w) {
        this.tickSize = tickSize;
        this.cycleSize = cycleSize;
        this.w = w;
        this.gui = w.gui;
        this.events = new ArrayList<Event> ();
        this.r = w.getR ();
    }

    @Override
    public void run() {
        while (isAlive () && !isInterrupted ()) {
            if (running) {
                loop ();
            } else {
                if (loop) {
                    loop = false;
                    loop ();
                }
                try {
                    Thread.sleep (50);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    public void loop() {
        try {
            double elapsed = 0;
            long start = System.nanoTime ();
            if (!gui.panel.spheed) {
                CountDownLatch latch = new CountDownLatch (1);
                gui.update (latch);
                latch.await ();
            }
            lastPop = w.getCritters ().size ();
            times = new HashMap<String, Double> ();
            totals = new double[]{0, 0, 0};
            tick ();
            System.out.println (totals[0] + ", " + totals[1] + ", " + totals[2]);
            System.out.println (times.toString ());
            System.out.println ("Tick: " + (System.nanoTime () - start) / 1000000);
            ticks++;
            if (ticks >= cycleSize) {
                cycle ();
                ticks = 0;
            }
            elapsed = (double) (System.nanoTime () - start) / 1000000;
            gui.lastCycleTime = (int) elapsed;
            gui.panel.totalAmount = w.getCritters ().size ();
            if (elapsed < tickSize && tickSize != 0)
                sleep ((long) tickSize - (long) elapsed);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    public void tick() {
        for (Critter c : new Vector<Critter> (w.getCritters ())) {
            if (c.isAlive ()) {
                c.tick ();
            } else {
                Foods foods = c.getCell ().getFoods ();
                double amount = c.getBiomass () / 4;  //CORPSE FOOD VALUE
                if (foods != null) {
                    if (foods.getFoodTypes ().contains (6)) {
                        int index = foods.getFoodTypes ().indexOf (6);
                        foods.addFoodToExisting (index, amount);
                    } else {
                        foods.addFood (-0.15, 8, amount, Double.MAX_VALUE);
                    }
                } else {
                    foods = new Foods (c.getEnviro ());
                    foods.addFood (-0.15, 8, amount, Double.MAX_VALUE);
                    c.getCell ().setFoods (foods);
                }
                w.getCritters ().remove (c);
                c.getEnviro ().getCritters ().remove (c);
            }
        }
    }

    public void cycle() {
        generateEvents ();
        cycleEvents ();
        cycleWorld ();
        //w.panel2.repaint ();
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
        //System.out.println ("Ciclo generazione: " + (double) (System.nanoTime () - start) / 1000000);
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
        //System.out.println ("Ciclo eventi: " + (double) (System.nanoTime () - start) / 1000000);
    }

    public void cycleWorld() {
        long start = System.nanoTime ();
        count = 0;
        for (ArrayList<Enviro> row : w.getMap ()) {
            for (Enviro e : row) {
                cycleEnviro (e);
            }
        }
        //System.out.println ("Ciclo celle: " + (double) (System.nanoTime () - start) / 1000000);
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
                e.getGrid ()[i][j].tick ();
            }
        }
    }
}
