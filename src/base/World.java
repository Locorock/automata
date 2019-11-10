package base;

import cells.RiverWater;
import enumLists.EnviroList;
import graphics.TestRender;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class World {
    private ArrayList<Enviro> enviros = new ArrayList<Enviro> ();
    private int size;
    private ArrayList<ArrayList<Enviro>> map;
    private Random r;

    public World(int size) {
        this.size = size;
        int seed = new Random ().nextInt (10000);
        this.r = new Random (seed);
        //generateWorldExp(size, 30,25,100); //DA RIMETTERE HUM A 20
        generateWorldNobs (size, 25, 25, 150);
    }

    public static void main(String[] args) {
        World w = new World (10);
        Time t = new Time (100, 20, w);
        JFrame jf = new JFrame ();
        jf.setSize (800, 830);
        jf.setVisible (true);
        TestRender jp = new TestRender (w.map);
        jf.add (jp);
        System.out.println ("test");
        //t.start ();
    }

    public String assignBiome(Enviro e) {
        double temp = e.getTemperature ();
        double hum = e.getHumidity ();

        ArrayList<Double> wProbs = new ArrayList<> ();
        double tProb = 0;
        for (EnviroList el : EnviroList.values ()) {
            double wProb = 0;
            if (temp >= el.getTempMin () && temp <= el.getTempMax () && hum >= el.getHumMin () && hum <= el.getHumMax ()) {
                double diffT = Math.abs (temp - (el.getTempMin () + el.getTempMax ())) / 2;
                double diffH = Math.abs (hum - (el.getHumMin () + el.getHumMax ())) / 2;
                wProb = ((diffT + diffH)) / el.getRarity ();

            }
            wProbs.add (wProb);
            tProb += wProb;
        }
        double selection = r.nextDouble () * tProb;
        double partial = 0;
        for (int i = 0; i < wProbs.size (); i++) {
            partial += wProbs.get (i);
            if (selection <= partial) {
                System.out.println (EnviroList.values ()[i].name () + " found for: " + e.getTemperature () + ", " + e.getHumidity ());
                return EnviroList.values ()[i].name ();
            }
        }
        return "";
    }

    public void generateWorldNobs(int max, double temp, double hum, int alt) {
        Enviro start = new Enviro (temp, alt, hum, "Plains", this, r);
        map = new ArrayList<ArrayList<Enviro>> ();
        for (int i = 0; i < 400; i++) {
            map.add (new ArrayList<> ());
            for (int j = 0; j < 400; j++) {
                map.get (i).add (null);
            }
        }
        start.generate ();
        map.get (200).set (200, start);
        enviros.add (start);
        boolean added = true;
        while (added) {
            added = false;
            for (int i = 0; i < map.size (); i++) {
                for (int j = 0; j < map.get (i).size (); j++) {
                    if (map.get (i).get (j) != null) {
                        for (int k = -1; k <= 1; k++) {
                            for (int l = -1; l <= 1; l++) {
                                if (!(k == 0 && l == 0) && Math.abs (l + k) == 1) {
                                    if (map.get (i + k).get (j + l) == null && map.get (i).get (j).getDistance () < max) {
                                        Enviro env = new Enviro (0, 0, 0, "", this, r);
                                        env.setX (i + k);
                                        env.setY (j + l);
                                        env.inheritStats (map.get (i).get (j), k, l);
                                        map.get (i + k).set (j + l, env);
                                        env.setBiome (assignBiome (env));
                                        env.generate ();
                                        enviros.add (env);
                                        added = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        outfitMap ();
        generateRivers ();
        printBiomes ();
    }

    public void outfitMap() {
        for (int i = 0; i < map.size (); i++) {
            boolean empty = true;
            for (int j = 0; j < map.get (i).size (); j++) {
                if (map.get (i).get (j) != null) {
                    empty = false;
                }
            }
            if (empty) {
                map.remove (i);
                i--;
            }
        }
        int found = Integer.MAX_VALUE;
        for (int i = 0; i < map.size (); i++) {
            for (int j = 0; j < map.get (i).size (); j++) {
                if (map.get (i).get (j) != null) {
                    if (j < found) {
                        found = j;
                    }
                }
            }
        }
        for (int j = 0; j < found; j++) {
            for (int i = 0; i < map.size (); i++) {
                map.get (i).remove (0);
            }
        }

        int maxE = 0;
        for (int i = 0; i < map.size (); i++) {
            for (int j = 0; j < map.get (i).size (); j++) {
                if (map.get (i).get (j) != null) {
                    if (j > maxE)
                        maxE = j;
                }
            }
        }


        for (int i = 0; i < map.size (); i++) {
            for (int j = maxE; j < map.get (i).size (); j++) {
                map.get (i).remove (j);
                j--;
            }
        }

        for (int i = 0; i < map.size (); i++) {
            for (int j = 0; j < map.get (i).size (); j++) {
                if (map.get (i).get (j) != null) {
                    map.get (i).get (j).setY (i);
                    map.get (i).get (j).setX (j);
                }
            }
        }
    }

    public void generateRivers() {
        int nRiviello = r.nextInt (3) + size / 4;
        if (nRiviello < 0) {
            nRiviello = 1;
        }
        ;
        double[][] topMax = new double[nRiviello][3];
        for (int k = 0; k < nRiviello; k++) {
            topMax[k] = new double[]{0, 1000, 1000};
        }
        for (int k = 0; k < nRiviello; k++) {
            for (int i = 0; i < map.size (); i++) {
                for (int j = 0; j < map.get (i).size (); j++) {
                    if (map.get (i).get (j) != null) {
                        if (map.get (i).get (j).getAltitude () > topMax[k][0]) {
                            boolean valid = true;
                            for (int l = 0; l < nRiviello; l++) {
                                if (l != k && Math.abs (topMax[l][1] - i) + Math.abs (topMax[l][2] - j) < 8) {
                                    valid = false;
                                }
                            }
                            if (valid) {
                                topMax[k][0] = map.get (i).get (j).getAltitude ();
                                topMax[k][1] = i;
                                topMax[k][2] = j;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < nRiviello; i++) {
            Enviro e = map.get ((int) topMax[i][1]).get ((int) topMax[i][2]);
            e.setRiver (true);
            flowRiverMap (e, -1, new ArrayList<Enviro> () {
            });
        }
    }

    public void flowRiverMap(Enviro enviro, int dirFrom, ArrayList<Enviro> antiLoop) {
        if (enviro == null) {
            return;
        }
        Enviro next = null;
        double alt = enviro.getAltitude ();
        double min = Integer.MAX_VALUE;
        int x = enviro.getX ();
        int y = enviro.getY ();
        Enviro up = map.get (y - 1).get (x);
        Enviro right = map.get (y).get (x + 1);
        Enviro down = map.get (y + 1).get (x);
        Enviro left = map.get (y).get (x - 1);

        if (up == null || right == null || left == null || down == null) {
            int dirTo;
            if (up == null) {
                dirTo = 0;
            } else {
                if (down == null) {
                    dirTo = 2;
                } else {
                    if (right == null) {
                        dirTo = 1;
                    } else {
                        dirTo = 3;
                    }
                }
            }
            fillRiver (enviro, dirFrom, dirTo);
            enviro.setRiver (true);
            return;
        } else {
            int dirTo = 0;
            if (!antiLoop.contains (up) && up.getAltitude () < min) {
                next = up;
                min = next.getAltitude ();
                dirTo = 0;
            }
            if (!antiLoop.contains (down) && down.getAltitude () < min) {
                next = down;
                min = next.getAltitude ();
                dirTo = 2;
            }
            if (!antiLoop.contains (right) && right.getAltitude () < min) {
                next = right;
                min = next.getAltitude ();
                dirTo = 1;
            }
            if (!antiLoop.contains (left) && left.getAltitude () < min) {
                next = left;
                min = next.getAltitude ();
                dirTo = 3;
            }
            enviro.setRiver (true);
            antiLoop.add (enviro);

            if (dirFrom == 0) {
                System.out.println ("UP");
            } else {
                if (dirFrom == 1) {
                    System.out.println ("RIGHT");
                } else {
                    if (dirFrom == 2) {
                        System.out.println ("DOWN");
                    } else {
                        if (dirFrom == -1) {
                            System.out.println ("START");
                        } else {
                            System.out.println ("LEFT");
                        }
                    }
                }
            }
            if (dirTo == 0) {
                System.out.println ("UP");
            } else {
                if (dirTo == 1) {
                    System.out.println ("RIGHT");
                } else {
                    if (dirTo == 2) {
                        System.out.println ("DOWN");
                    } else {
                        System.out.println ("LEFT");
                    }
                }
            }
            fillRiver (enviro, dirFrom, dirTo);
            flowRiverMap (next, dirTo, antiLoop);

        }
    }

    public void fillRiver(Enviro e, int from, int to) {
        if (from != -1) {
            from = (from + 2) % 4;
        }
        System.out.println ("DIR3 " + to);
        int[] dirs = new int[]{from, to};
        for (int i = 0; i < 2; i++) {
            if (dirs[i] == -1) {
                continue;
            }
            int startX = 0, startY = 0;
            boolean vertical = false;
            boolean incremental = true;
            switch (dirs[i]) {
                case 0: {
                    startX = 8;
                    startY = 0;
                    vertical = true;
                    incremental = true;
                    break;
                }
                case 1: {
                    startX = 15;
                    startY = 8;
                    vertical = false;
                    incremental = false;
                    break;
                }
                case 2: {
                    startX = 8;
                    startY = 15;
                    vertical = true;
                    incremental = false;
                    break;
                }
                case 3: {
                    startX = 0;
                    startY = 8;
                    vertical = false;
                    incremental = true;
                    break;
                }
                default:
                    throw new IllegalStateException ("Unexpected value: " + dirs[i]);
            }
            double leftSize = 1;
            double rightSize = 1;

            int x = startX;
            while (true) {
                if (incremental) {
                    if (!(x <= 8)) break;
                } else {
                    if (!(x >= 8)) break;
                }
                int y = startY;
                while (true) {
                    if (incremental) {
                        if (!(y <= 8)) break;
                    } else {
                        if (!(y >= 8)) break;
                    }
                    if (vertical) {
                        for (int j = (int) (-leftSize + startX); j <= rightSize + startX; j++) {
                            e.getGrid ()[j][y] = new RiverWater ("RiverWater", e);
                        }
                    } else {
                        for (int j = (int) (-leftSize + startY); j <= rightSize + startY; j++) {
                            e.getGrid ()[x][j] = new RiverWater ("RiverWater", e);
                        }
                    }
                    leftSize = leftSize + r.nextGaussian () / 4;
                    rightSize = rightSize + r.nextGaussian () / 4;
                    if (leftSize < 0) {
                        leftSize = 0;
                    }
                    if (rightSize < 0) {
                        rightSize = 0;
                    }
                    if (incremental) {
                        y++;
                    } else {
                        y--;
                    }
                }
                if (incremental) {
                    x++;
                } else {
                    x--;
                }
            }
        }
        System.out.println (e.getX () + " - " + e.getY ());
        //e.printGrid ();

    }

    public void printWhole() {
        for (ArrayList<Enviro> row : map) {
            for (int i = 0; i < 16; i++) {
                for (Enviro enviro : row) {
                    for (int j = 0; j < 16; j++) {
                        if (enviro == null) {
                            System.out.print ("000");
                        } else {
                            System.out.print ((enviro.getGrid ()[j][i].getType () + "").substring (0, 3));
                        }
                    }
                }
                System.out.println ();
            }
        }
    }

    public void printBiomes() {
        for (int i = 0; i < map.size (); i++) {
            for (int j = 0; j < map.get (i).size (); j++) {
                if (map.get (i).get (j) == null) {
                    System.out.print ("000");
                } else {
                    System.out.print (map.get (i).get (j).getBiome ().substring (0, 2) + " ");
                }
            }
            System.out.println ();
        }
    }

    public int[] getPosition(Enviro enviro) {
        for (int i = 0; i < map.size (); i++) {
            for (int j = 0; j < map.size (); j++) {
                if (map.get (i).get (j).equals (enviro)) {
                    return new int[]{j, i};
                }
            }
        }
        return null;
    }

    public ArrayList<Enviro> getEnviros() {
        return enviros;
    }

    public void setEnviros(ArrayList<Enviro> enviros) {
        this.enviros = enviros;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Random getR() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }

    public ArrayList<ArrayList<Enviro>> getMap() {
        return map;
    }

    public void setMap(ArrayList<ArrayList<Enviro>> map) {
        this.map = map;
    }
}
