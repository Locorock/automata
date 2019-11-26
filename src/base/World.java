package base;

import cells.RiverWater;
import critters.Critter;
import enumLists.EnviroList;
import graphics.WorldRender;

import javax.swing.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;
import java.util.Vector;

public class World {
    private ArrayList<ArrayList<Enviro>> map;
    private Random r;
    private static final int size = 8;
    private ArrayList<Enviro> enviros = new ArrayList<> ();
    private BitSet[] cellId;
    private int enviroWidth = Enviro.width;
    public WorldRender panel;
    private int fullWidth;
    private int fullHeight;
    private Vector<Critter> critters = new Vector<Critter> ();

    public World() {
        int seed = new Random ().nextInt (10000);
        this.r = new Random (seed);
        generateWorldNobs (size, 20, 40, 80);
        this.fullHeight = this.map.size () * enviroWidth;
        this.fullWidth = this.map.get (0).size () * enviroWidth;
        Critter.weights = new double[getFullHeight ()][getFullWidth ()];
        for (int i = 0; i < 15; i++) {
            Critter c2 = new Critter ("Katrina", this, 15 * 16 + 5, 15 * 16 + 5);
            critters.add (c2);
        }
        JFrame jf = new JFrame ();
        jf.setSize (800, 830);
        jf.setVisible (true);
        WorldRender jp = new WorldRender (this, jf);
        jf.add (jp);
        this.panel = jp;
        Time t = new Time (10, 20, this);
        t.start ();
    }

    public static void main(String[] args) {
        World w = new World ();

    }

    private String assignBiome(Enviro e) {
        double temp = e.getAvgTemp ();
        double hum = e.getAvgHum ();

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
                return EnviroList.values ()[i].name ();
            }
        }
        return "";
    }

    private void generateWorldNobs(int max, double temp, double hum, int alt) {
        Enviro start = new Enviro (temp, alt, hum, "Plains", this, r);
        start.setHumidity (hum);
        start.setTemperature (temp);
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
        //smoothMap ();
        generateRivers ();
        fillOcean ();
        coordinateCells ();
        printBiomes ();
    }

    private void fillOcean() {
        for (ArrayList<Enviro> row : map) {
            for (int i = 0; i < row.size (); i++) {
                if (row.get (i) == null) {
                    row.set (i, new Enviro (20, 0, 20, "Ocean", this, r));
                    row.get (i).generate ();
                }
            }
        }
    }

    private void coordinateCells() {
        for (int i = 0; i < map.size (); i++) {
            for (int j = 0; j < map.get (i).size (); j++) {
                Enviro e = map.get (i).get (j);
                if (e != null) {
                    int width = e.getWidth ();
                    for (int k = 0; k < width; k++) {
                        for (int l = 0; l < width; l++) {
                            e.getGrid ()[k][l].setAbsX (width * j + l);
                            e.getGrid ()[k][l].setAbsY (width * i + k);
                        }
                    }
                }
            }
        }
    }

    private void smoothMap() {
        int sumT = 0, sumH = 0, count = 0;
        for (ArrayList<Enviro> enviroArrayList : map) {
            for (Enviro enviro : enviroArrayList) {
                if (enviro != null) {
                    sumT = (int) enviro.getAvgTemp ();
                    sumH = (int) enviro.getAvgHum ();
                    count++;
                }
            }
        }
        sumT /= count;
        sumH /= count;
        for (ArrayList<Enviro> enviroArrayList : map) {
            for (int j = 0; j < enviroArrayList.size (); j++) {
                if (enviroArrayList.get (j) != null) {
                    Enviro e = enviroArrayList.get (j);
                    e.setAvgTemp (e.getAvgTemp () + (sumT - e.getAvgTemp ()) / 4);
                    e.setAvgHum (e.getAvgHum () + (sumH - e.getAvgHum ()) / 4);
                }
            }
        }
    }

    private void outfitMap() {
        ArrayList<ArrayList<Enviro>> keep = new ArrayList<> ();
        for (int i = 0; i < map.size (); i++) {
            boolean empty = true;
            for (int j = 0; j < map.get (i).size (); j++) {
                if (map.get (i).get (j) != null) {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                keep.add (map.remove (i));
                i--;
            }
        }

        map.add (0, keep.remove (0));
        map.add (0, keep.remove (0));
        map.add (keep.remove (0));
        map.add (keep.remove (0));

        int found = Integer.MAX_VALUE;
        for (ArrayList<Enviro> enviroArrayList : map) {
            for (int j = 0; j < enviroArrayList.size (); j++) {
                if (enviroArrayList.get (j) != null) {
                    if (j < found) {
                        found = j;
                    }
                }
            }
        }
        for (int j = 0; j < found - 2; j++) {
            for (int i = 0; i < map.size (); i++) {
                map.get (i).remove (0);
            }
        }

        int maxE = 0;
        for (ArrayList<Enviro> enviroArrayList : map) {
            for (int j = 0; j < enviroArrayList.size (); j++) {
                if (enviroArrayList.get (j) != null) {
                    if (j > maxE)
                        maxE = j;
                }
            }
        }


        for (ArrayList<Enviro> enviroArrayList : map) {
            for (int j = maxE + 3; j < enviroArrayList.size (); j++) {
                enviroArrayList.remove (j);
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

    private void generateRivers() {
        int nRiviello = r.nextInt (3) + size / 2;
        if (nRiviello < 0) {
            nRiviello = 1;
        }
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
                                if (l != k && Math.abs (topMax[l][1] - i) + Math.abs (topMax[l][2] - j) < 6) {
                                    valid = false;
                                    break;
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
            flowRiverMap (e, -1, new ArrayList<> ());
        }
    }

    private void flowRiverMap(Enviro enviro, int dirFrom, ArrayList<Enviro> antiLoop) {
        if (enviro == null) {
            return;
        }
        Enviro next = null;
        double alt = enviro.getAltitude ();
        double min = Integer.MAX_VALUE;
        int x = enviro.getX ();
        int y = enviro.getY ();

        Enviro up = null, right = null, down = null, left = null;
        if (y - 1 >= 0) {
            up = map.get (y - 1).get (x);
        }
        if (x + 1 < map.get (y).size ()) {
            right = map.get (y).get (x + 1);
        }
        if (y + 1 < map.size ()) {
            down = map.get (y + 1).get (x);
        }
        if (x - 1 >= 0) {
            left = map.get (y).get (x - 1);
        }





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
                dirTo = 3;
            }
            enviro.setRiver (true);
            antiLoop.add (enviro);
            fillRiver (enviro, dirFrom, dirTo);
            flowRiverMap (next, dirTo, antiLoop);

        }
    }

    private void fillRiver(Enviro e, int from, int to) {
        if (from != -1) {
            from = (from + 2) % 4;
        }
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
                    startX = enviroWidth / 2;
                    startY = 0;
                    vertical = true;
                    incremental = true;
                    break;
                }
                case 1: {
                    startX = enviroWidth - 1;
                    startY = enviroWidth / 2;
                    vertical = false;
                    incremental = false;
                    break;
                }
                case 2: {
                    startX = enviroWidth / 2;
                    startY = enviroWidth - 1;
                    vertical = true;
                    incremental = false;
                    break;
                }
                case 3: {
                    startX = 0;
                    startY = enviroWidth / 2;
                    vertical = false;
                    incremental = true;
                    break;
                }
                default:
                    throw new IllegalStateException ("Unexpected value: " + dirs[i]);
            }
            double leftSize = 2;
            double rightSize = 2;

            int x = startX;
            while (true) {
                if (incremental) {
                    if (!(x <= enviroWidth / 2)) break;
                } else {
                    if (!(x >= enviroWidth / 2)) break;
                }
                int y = startY;
                while (true) {
                    if (incremental) {
                        if (!(y <= enviroWidth / 2)) break;
                    } else {
                        if (!(y >= enviroWidth / 2)) break;
                    }
                    if (vertical) {
                        for (int j = (int) (-leftSize + startX); j <= rightSize + startX; j++) {
                            e.getGrid ()[y][j] = new RiverWater ("RiverWater", e);
                        }
                    } else {
                        for (int j = (int) (-leftSize + startY); j <= rightSize + startY; j++) {
                            e.getGrid ()[j][x] = new RiverWater ("RiverWater", e);
                        }
                    }
                    leftSize = leftSize + r.nextGaussian () / 3;
                    rightSize = rightSize + r.nextGaussian () / 3;
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
    }

    public Cell getAbsCell(int absx, int absy) {
        try {
            return getMap ().get (absy / enviroWidth).get (absx / enviroWidth).getGrid ()[absy % enviroWidth][absx % enviroWidth];
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace ();
            System.out.println (absy + "/" + enviroWidth + "=" + absy / enviroWidth);
            System.out.println (absx + "/" + enviroWidth + "=" + absx / enviroWidth);
            System.out.println (absx + "%" + enviroWidth + "=" + absx % enviroWidth);
            System.out.println (absy + "%" + enviroWidth + "=" + absy % enviroWidth);
            System.out.println (this.map.size ());
            System.out.println (this.getFullHeight ());
        }
        return null;
    }

    public void printBiomes() {
        for (ArrayList<Enviro> enviroArrayList : map) {
            for (int j = 0; j < enviroArrayList.size (); j++) {
                if (enviroArrayList.get (j) == null) {
                    System.out.print ("000");
                } else {
                    System.out.print (enviroArrayList.get (j).getBiome ().substring (0, 2) + " ");
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

    public int getFullWidth() {
        return fullWidth;
    }

    public void setFullWidth(int fullWidth) {
        this.fullWidth = fullWidth;
    }

    public int getFullHeight() {
        return fullHeight;
    }

    public void setFullHeight(int fullHeight) {
        this.fullHeight = fullHeight;
    }

    public Vector<Critter> getCritters() {
        return critters;
    }

    public void setCritters(Vector<Critter> critters) {
        this.critters = critters;
    }
}
