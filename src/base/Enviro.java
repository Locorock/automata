package base;

import enumLists.CellList;

import java.util.ArrayList;
import java.util.Random;

import static base.Cell.makeCell;

public class Enviro {
    private Enviro enviroUp;
    private Enviro enviroDown;
    private Enviro enviroLeft;
    private Enviro enviroRight;
    private double distance;
    private World world;
    private String biome;

    private Cell[][] grid;
    private int width = 16;
    private double temperature, humidity, altitude;
    private Random r;
    private boolean river;
    private int x, y;

    private int rainStr, quakeStr, lightningStr, eruptionS;

    public Enviro(double temperature, int altitude, double humidity, String biome, World world, Random r) {
        this.temperature = temperature;
        this.altitude = altitude;
        this.humidity = humidity;
        this.biome = biome;
        this.world = world;
        this.r = r;
        initGrid ();
    }

    public Enviro(int x, int y, World world, Random r) {
        this.world = world;
        this.r = r;
        this.x = x;
        this.y = y;
    }

    private void initGrid() {
        this.width = 16;
        grid = new Cell[width][width];
        for (int i = 0; i < width; i++) {
            grid[i] = new Cell[width];
        }
    }

    public void inheritStats(Enviro parent1, int dir1x, int dir1y) {
        Enviro parent2 = null;
        ArrayList<ArrayList<Enviro>> map = world.getMap ();
        linkDir (parent1, dir1x, dir1y);
        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                if (!(k == 0 && l == 0) && Math.abs (l + k) == 1) {
                    if (map.get (x + k).get (y + l) != null) {
                        if (parent1 != map.get (x + k).get (y + l)) {
                            parent2 = map.get (x + k).get (y + l);
                            System.out.println (parent1 + " " + parent2);
                            linkDir (parent2, -k, -l);
                        }
                    }
                }
            }
        }
        if (parent2 == null) {
            this.temperature = parent1.getTemperature ();
            this.humidity = parent1.getHumidity ();
            this.altitude = parent1.getAltitude ();
            this.distance = parent1.getDistance ();
        } else {
            this.temperature = (parent1.getTemperature () + parent2.getTemperature ()) / 2;
            this.humidity = (parent1.getHumidity () + parent2.getHumidity ()) / 2;
            this.altitude = (parent1.getAltitude () + parent2.getAltitude ()) / 2;
            this.distance = (parent1.getDistance () + parent2.getDistance ()) / 2;
        }
        this.distance += 1 + ((r.nextGaussian () - 0.5) / 1.8); // 1.8 def
        this.temperature += r.nextGaussian () * 4;  //VARIANZA STATISTICHE MONDO
        this.humidity += r.nextGaussian () * 4;
        this.humidity = Math.abs (this.humidity);
        this.altitude = this.altitude + (r.nextGaussian () * 3 - 2);
    }

    public void linkDir(Enviro parent, int dirx, int diry) {
        if (dirx == 1 && diry == 0) {
            parent.setEnviroRight (this);
            this.setEnviroLeft (parent);
        }
        if (dirx == -1 && diry == 0) {
            parent.setEnviroLeft (this);
            this.setEnviroRight (parent);
        }
        if (dirx == 0 && diry == 1) {
            parent.setEnviroDown (this);
            this.setEnviroUp (parent);
        }
        if (dirx == 0 && diry == -1) {
            parent.setEnviroUp (this);
            this.setEnviroDown (parent);
        }
    }

    public void generate() {

        ArrayList<Integer> elements = new ArrayList<> ();
        ArrayList<String> grounds = new ArrayList<> ();
        for (int i = 1; i < CellList.values ().length; i++) {
            CellList type = CellList.values ()[i];
            if (type.getBiomes ().contains (this.biome)) {
                if (type.getPhase () == 1) {
                    grounds.add (type.name ());
                    elements.add (-1);
                } else {
                    elements.add ((int) Math.round (((this.humidity / 20) + Math.abs (r.nextGaussian () / 2)) * type.getHumMult ()));
                }
            } else {
                elements.add (0);
            }
        }
        int prob;
        CellList type1 = CellList.valueOf (grounds.get (0));
        CellList type2;
        if (grounds.size () == 1) {
            type2 = CellList.valueOf ("None");
            prob = 100;
        } else {
            type2 = CellList.valueOf (grounds.get (1));
            if (type1.getHumMult () == -1 && type2.getHumMult () == 1 || type1.getHumMult () == 1 && type2.getHumMult () == -1) {
                prob = (int) (Math.floor (20 * Math.log (humidity) + 20) - Math.floor ((20 * Math.log (humidity) + 20) % 100) * Math.floor ((20 * Math.log (humidity) + 20) / 100));
            } else {
                prob = 50;
            }
        }

        elements.set (elements.indexOf (-1), prob);
        if (!type2.name ().equals ("None")) {
            elements.set (elements.indexOf (-1), 100 - prob);
        }

        for (int i = 1; i <= 3; i++) {
            for (int j = 0; j < elements.size (); j++) {
                CellList type = CellList.values ()[j + 1];
                if (type.getPhase () == i) {
                    if (i == 1 && elements.get (j) > 0) {
                        if (grounds.size () < 2) {
                            paintGround (type.name (), null, 100);
                        } else {
                            paintGround (type1.name (), type2.name (), elements.get (j));
                        }
                        j = elements.size ();
                    } else {
                        boolean[][] filled = new boolean[16][16];
                        for (int k = 0; k < elements.get (j); k++) {
                            int x = r.nextInt (16);
                            int y = r.nextInt (16);
                            if (!filled[x][y]) {
                                grid[x][y] = makeCell (type.name (), this, null);
                                filled[x][y] = true;
                            } else {
                                k--;
                            }
                        }
                    }
                }
            }
        }
        System.out.println (CellList.values ().toString ());
        System.out.println (elements.toString ());
        printGrid ();
    }

    public void printGrid() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[j][i] == null) {
                    System.out.print ("00 ");
                } else {
                    System.out.print (grid[j][i].getType ().substring (0, 3) + "");
                }
            }
            System.out.println ();
        }
    }

    private void paintGround(String groundA, String groundB, int groundAPortion) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                int random = r.nextInt (100);
                String groundName;
                if (random < groundAPortion) {
                    groundName = groundA;
                } else {
                    groundName = groundB;
                }
                grid[j][i] = makeCell (groundName, this, null);
            }
        }
    }

    public Enviro getEnviroUp() {
        return enviroUp;
    }

    public void setEnviroUp(Enviro enviroUp) {
        this.enviroUp = enviroUp;
    }

    public Enviro getEnviroDown() {
        return enviroDown;
    }

    public void setEnviroDown(Enviro enviroDown) {
        this.enviroDown = enviroDown;
    }

    public Enviro getEnviroLeft() {
        return enviroLeft;
    }

    public void setEnviroLeft(Enviro enviroLeft) {
        this.enviroLeft = enviroLeft;
    }

    public Enviro getEnviroRight() {
        return enviroRight;
    }

    public void setEnviroRight(Enviro enviroRight) {
        this.enviroRight = enviroRight;
    }

    public ArrayList<Enviro>[] scanNeighbours(int dim) {
        ArrayList<Enviro>[] scanned = new ArrayList[dim];

        for (int i = 0; i < dim; i++) {
            scanned[i] = new ArrayList<> ();
        }
        scanned[0].add (this);

        if (this.getEnviroRight () != null)
            scan (this.getEnviroRight (), 1, dim, scanned, 0);
        if (this.getEnviroDown () != null)
            scan (this.getEnviroDown (), 1, dim, scanned, 1);
        if (this.getEnviroLeft () != null)
            scan (this.getEnviroLeft (), 1, dim, scanned, 2);
        if (this.getEnviroUp () != null)
            scan (this.getEnviroUp (), 1, dim, scanned, 3);

        return scanned;
    }

    public void scan(Enviro enviro, int distance, int dim, ArrayList<Enviro>[] scanned, int quadrant) {
        if (distance >= dim) {
            return;
        }
        boolean tag = false;
        for (int i = 0; i < scanned.length; i++) {
            if (scanned[i].contains (enviro)) {
                tag = true;
            }
        }
        if (tag == false) {
            scanned[distance].add (enviro);
            switch (quadrant) {
                case 0: {
                    if (enviro.getEnviroUp () != null)
                        scan (enviro.getEnviroUp (), distance + 1, dim, scanned, 0);
                    if (enviro.getEnviroRight () != null)
                        scan (enviro.getEnviroRight (), distance + 1, dim, scanned, 0);
                    break;
                }
                case 1: {
                    if (enviro.getEnviroRight () != null)
                        scan (enviro.getEnviroRight (), distance + 1, dim, scanned, 1);
                    if (enviro.getEnviroDown () != null)
                        scan (enviro.getEnviroDown (), distance + 1, dim, scanned, 1);
                    break;
                }
                case 2: {
                    if (enviro.getEnviroDown () != null)
                        scan (enviro.getEnviroDown (), distance + 1, dim, scanned, 2);
                    if (enviro.getEnviroLeft () != null)
                        scan (enviro.getEnviroLeft (), distance + 1, dim, scanned, 2);
                    break;
                }
                case 3: {
                    if (enviro.getEnviroLeft () != null)
                        scan (enviro.getEnviroLeft (), distance + 1, dim, scanned, 3);
                    if (enviro.getEnviroUp () != null)
                        scan (enviro.getEnviroUp (), distance + 1, dim, scanned, 3);
                    break;
                }
            }
        }
    }

    public void replaceWith(Cell oldCell, Cell newCell) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[j][i] == oldCell) {
                    grid[j][i] = newCell;
                }
            }
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getBiome() {
        return biome;
    }

    public void setBiome(String biome) {
        this.biome = biome;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Random getR() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getRainStr() {
        return rainStr;
    }

    public void setRainStr(int rainStr) {
        this.rainStr = rainStr;
    }

    public int getQuakeStr() {
        return quakeStr;
    }

    public void setQuakeStr(int quakeStr) {
        this.quakeStr = quakeStr;
    }

    public int getLightningStr() {
        return lightningStr;
    }

    public void setLightningStr(int lightningStr) {
        this.lightningStr = lightningStr;
    }

    public int getEruptionS() {
        return eruptionS;
    }

    public void setEruptionS(int eruptionS) {
        this.eruptionS = eruptionS;
    }

    public boolean isRiver() {
        return river;
    }

    public void setRiver(boolean river) {
        this.river = river;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
