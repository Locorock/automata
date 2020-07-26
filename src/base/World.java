package base;

import cells.RiverWater;
import enumLists.EnviroList;
import graphics.AdvancedWorldRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static base.SimplexNoise.generateOctavedSimplexNoise;

public class World {
    public static final int tickSpeed = 0;
    public static final int critterAmount = 0;
    private static final int size = 64;

    private ArrayList<ArrayList<Enviro>> map;
    private Random r;
    private ArrayList<Enviro> enviros = new ArrayList<> ();
    private BitSet[] cellId;
    private final int enviroWidth = Enviro.width;
    public AdvancedWorldRenderer panel;
    private int fullWidth;
    private int fullHeight;
    private Time t;
    private TreeSet<Critter> critters = new TreeSet<Critter> (Critter::compareTo);
    public ArrayDeque<Cell> cells = new ArrayDeque<> ();
    public ArrayDeque<Cell> updates = new ArrayDeque<> ();

    public World() {
        int seed = new Random ().nextInt (10000);
        this.r = new Random (seed);
        //generateWorldNobs (size, 20, 30, 80);
        generateWorldSimplex (size, 0, 0);
        GeneLibrary gl = new GeneLibrary ();
        this.fullHeight = this.map.size () * enviroWidth;
        this.fullWidth = this.map.get (0).size () * enviroWidth;
        DecisionalCore.weights = new double[getFullHeight ()][getFullWidth ()];
        for (int i = 0; i < critterAmount; i++) {
            Critter c2 = new Critter ("Katrina", this, 15 * 16 + 5, 15 * 16 + 5);
            critters.add (c2);
        }
        JFrame jf = new JFrame ();
        jf.setSize (new Dimension (getFullWidth () * 2, getFullHeight () * 2));
        jf.setVisible (true);
        AdvancedWorldRenderer jp = new AdvancedWorldRenderer (this, jf);
        jf.add (jp);
        jp.repaint ();
        this.panel = jp;
        System.out.println ("Finished");
        t = new Time (tickSpeed, 20, this);
        t.start ();
        System.out.println ("Timing");
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

    private float[][] circularFilter(float[][] map) {
        float[][] result = new float[map.length][map.length];
        int centerX = size / 2, centerY = size / 2;
        double[][] grad = new double[map.length][map.length];
        double max = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                int distx = Math.abs (j - centerX) * 2;
                int disty = Math.abs (i - centerY) * 2;
                grad[i][j] = Math.sqrt (distx * distx + disty * disty);
                if (grad[i][j] > max) {
                    max = grad[i][j];
                }
            }
        }
        double max2 = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (grad[i][j] > max2) {
                    max2 = grad[i][j];
                }
            }
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                grad[i][j] = max2 - grad[i][j];
                grad[i][j] /= max2;
                result[i][j] = (float) ((grad[i][j] / 1.5) * map[i][j]);   //ATTENZIONE, ALTA MATEMATICA, CAMBIA IL NUMERO PER L'OFFSET E SBATTITENE
            }
        }
        for (int i = 0; i < grad.length; i++) {
            for (int j = 0; j < grad.length; j++) {
                System.out.print (" " + grad[i][j] + " ");
            }
            System.out.println ();
        }

        return result;
    }

    private float[][] circularFilterNew(float[][] map) {
        float[][] result = new float[map.length][map.length];
        int centerX = size / 2, centerY = size / 2;
        double[][] grad = new double[map.length][map.length];
        double max = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                int distx = Math.abs (j - centerX);
                int disty = Math.abs (i - centerY);
                grad[i][j] = 800 / (distx * distx + disty * disty + 1);
            }
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                result[i][j] = (float) ((grad[i][j]) * map[i][j]);   //ATTENZIONE, ALTA MATEMATICA, CAMBIA IL NUMERO PER L'OFFSET E SBATTITENE
            }
        }
        for (int i = 0; i < grad.length; i++) {
            for (int j = 0; j < grad.length; j++) {
                System.out.print (" " + grad[i][j] + " ");
            }
            System.out.println ();
        }

        return result;
    }

    private void generateWorldSimplex(int size, int skew, int magnitude) {
        float[][] heightMap = circularFilterNew (generateOctavedSimplexNoise (size, size, 4, 0.05f, 0.09f, r));
        float[][] heatMap = generateOctavedSimplexNoise (size, size, 3, 0.1f, 0.01f, r);
        float[][] humidityMap = generateOctavedSimplexNoise (size, size, 3, 0.1f, 0.01f, r);
        map = new ArrayList<ArrayList<Enviro>> ();
        for (int i = 0; i < size; i++) {
            map.add (new ArrayList<> ());
            for (int j = 0; j < size; j++) {
                double dist = (Math.abs (j - size / 2) + Math.abs (i - size / 2));
                double height = heightMap[i][j];
                double temp = Math.abs (heatMap[i][j]) * 100;
                double hum = Math.abs (humidityMap[i][j]) * 100 + 20;

                Enviro e = new Enviro (temp, height, hum, null, this, r, j, i);
                map.get (i).add (e);
                enviros.add (e);
            }
        }
        //generateRivers ();
        coordinateCells ();
        printBiomes ();
    }

    private void generateWorldNobs(int max, double temp, double hum, int alt) {
        Enviro start = new Enviro (temp, alt, hum, "PrimSoup", this, r);
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
        for (int i = 0; i < map.size (); i++) {
            for (int j = 0; j < map.get (i).size (); j++) {
                if (map.get (i).get (j) == null) {
                    map.get (i).set (j, new Enviro (20, 0, 20, "Ocean", this, r));
                    map.get (i).get (j).generate ();
                    map.get (i).get (j).setX (j);
                    map.get (i).get (j).setY (i);
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
            topMax[k] = new double[]{0, 0, 0};
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

    public TreeSet<Critter> getCritters() {
        return critters;
    }

    public void setCritters(TreeSet<Critter> critters) {
        this.critters = critters;
    }

    public Time getT() {
        return t;
    }

    public void setT(Time t) {
        this.t = t;
    }
}
