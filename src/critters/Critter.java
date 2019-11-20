package critters;

import base.Cell;
import base.Enviro;
import base.World;
import baseCells.Food;
import baseCells.FreshWater;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Critter {
    public ArrayDeque<Cell> path;
    private Enviro enviro;
    private GenCode code;
    public static long id = 0;
    public String name;
    private Cell cell;
    private ArrayList<WMap> wMaps = new ArrayList<> ();
    private int absx;
    private double thirst = 0;
    private int absy;
    private double hunger = 0;
    private int range = 6; //HARDCODED FOR NOW
    private double speed = 1;
    private double baseSpeed = 1;
    private double movementProgress = 0;
    private double maxThirst = 100;
    private double maxHunger = 100;
    private Critter mate;
    private boolean mothering = false;
    private int mateTolerance = 16;
    private boolean alive = true;
    private double age = 0;
    private World world;
    private WMap wMap;
    private double[][] paths;

    public Critter(String name, World w, int absx, int absy) {
        this (name, w, absx, absy, new GenCode (w.getR ()));
    }

    public Critter(String name, World w, int absx, int absy, Critter father, Critter mother) {
        this (name, w, absx, absy, new GenCode (father.getCode (), mother.getCode (), w.getR ()));
        this.hunger = 80;
        this.thirst = 80;
    }

    public Critter(String name, World w, int absx, int absy, GenCode code) {
        this.absx = absx;
        this.absy = absy;
        this.world = w;
        this.cell = world.getAbsCell (absx, absy);
        this.enviro = cell.getEnviro ();
        this.wMap = new WMap (this, world);
        this.name = name + "_" + id++;
        this.code = code;
        paths = new double[world.getFullHeight ()][world.getFullWidth ()];  //FRONTLOADED DECLARATION FOR PERFORMANCE
    }

    public void tick() {
        //AGING AND STATUS EFFECTS
        System.out.println (name + " - " + (hunger + "").substring (0, 2) + " - " + (thirst + "").substring (0, 2));
        if (this.thirst > this.maxThirst || this.hunger > this.maxHunger || age > 160) {
            this.alive = false;
            System.out.println ("dead");
            return;
        } else {
            this.thirst += 0.05;
            this.hunger += 0.1;
        }
        if (path == null || path.isEmpty ()) {
            if (mate != null) {
                System.out.println ("Mating");
                if (mate.getAbsx () == absx && mate.getAbsy () == absy) {
                    if (mothering) {
                        reproduce (mate);
                    } else {
                        this.mate = null;
                    }
                }
                return;
            }
            if (cell instanceof Food && ((Food) cell).getFoodAmount (0) > 1 && this.hunger > 0) {
                System.out.println ("Eating");
                eatOnCell ();
                return;
            }
            if (cell instanceof FreshWater && this.thirst > 0) {
                System.out.println ("Sippity");
                drinkOnCell ();
                return;
            }
            if (this.hunger < this.maxHunger / 3 && this.thirst < this.maxThirst / 3 && this.age > 30) {
                path = lookForMate ();
            }
            if (path == null || path.isEmpty ()) {
                path = choosePath (null);
            }
        } else {
            //MOVEMENT COST
            speed = baseSpeed * cell.getSpeedMod ();
            movementProgress += speed;
            while (movementProgress > 1) {
                movementProgress--;
                Cell next = path.removeFirst ();
                moveTo (next.getAbsX (), next.getAbsY ());
            }
            this.hunger += 0.05;
            this.thirst += 0.1;
        }
    }

    public ArrayDeque<Cell> lookForMate() {
        for (Critter critter : world.getCritters ()) {
            if (!critter.equals (this) && Math.abs (critter.absx - this.absx) < range && Math.abs (critter.absy - this.absy) < range) {
                int diff = critter.code.getHammingDiff ("AppearanceCluster", this.code.getGene ("AppearanceCluster"));
                if (diff < mateTolerance) {
                    if (mateHandshake (this, diff) == true) {
                        return choosePath (new int[]{critter.getAbsx (), critter.getAbsy ()});
                    }
                }
            }
        }
        return null;
    }

    public boolean mateHandshake(Critter critter, int diff) {
        if (critter.code.getHammingDiff ("AppearanceCluster", this.code.getGene ("AppearanceCluster")) < mateTolerance) {
            if (this.hunger < this.maxHunger / 3 && this.thirst < this.maxThirst / 3) {
                this.mate = critter;
                this.mothering = true;
                System.out.println ("Handshake");
                return true;
            }
        }
        return false;
    }

    public void reproduce(Critter father) {
        Critter child = new Critter ("Salino", world, absx, absy, father, this);
        world.getCritters ().add (child);
        this.hunger += 20;
        this.thirst += 20;
        this.mate = null;
        this.mothering = false;
    }

    public ArrayDeque<Cell> choosePath(int[] critPos) {
        paths = dijkPaths ();
        int destx = 0, desty = 0;
        if (critPos == null) {
            double max = 0;
            for (int i = -range / 2; i < range / 2; i++) {
                for (int j = -range / 2; j < range / 2; j++) {
                    double propension = paths[j + absx][i + absy];
                    Cell c = world.getAbsCell (j + absx, i + absy);
                    if (c instanceof Food) {
                        propension += hunger + ((Food) c).getFoodAmount (0);
                    }
                    if (c instanceof FreshWater) {
                        System.out.println ("Found sippity");
                        propension += thirst;
                    }
                    if (propension > max) {
                        max = propension;
                        destx = j + absx;
                        desty = i + absy;
                    }
                }
            }
        } else {
            destx = critPos[0];
            desty = critPos[1];
        }
        if (destx == absx && desty == absy) {
            return null;
        }
        return pathTo (destx, desty);
    }

    public ArrayDeque<Cell> pathTo(int destx, int desty) {
        ArrayDeque<Cell> path = new ArrayDeque<Cell> ();
        double min = Double.MAX_VALUE;
        int nextx = 0, nexty = 0;
        while (desty != this.absy && destx != this.absx) {
            path.addFirst (world.getAbsCell (destx, desty));
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0) && Math.abs (i + j) == 1) {
                        if (paths[i + desty][j + destx] < min) {
                            min = paths[i + desty][j + destx];
                            nextx = j + destx;
                            nexty = i + desty;
                        }
                    }
                }
            }
            destx = nextx;
            desty = nexty;
        }

        return path;
    }

    public void eatOnCell() {
        Food food = (Food) cell;
        food.onEat (this, 0);
        double amount = food.getFoodAmount (0) / 5;
        food.setFoodAmount (0, amount);
        double mod = 0;
        switch (food.getFoodType (0)) {
            case "Fruit": {
                mod = 0.5;
            }
            case "Meat": {
                mod = 1;
            }
            case "Leafage": {
                mod = 0.2;
            }
            case "Grass": {
                mod = 0.1;

            }
        }
        this.hunger -= mod * amount;
    }

    public void drinkOnCell() {
        ((FreshWater) this.cell).onDrink (this);
        this.thirst -= 3;

    }

    public void moveTo(int absx, int absy) {
        this.cell = world.getAbsCell (absx, absy);
        this.absx = absx;
        this.absy = absy;
        this.enviro = cell.getEnviro ();
    }


    public double[][] dijkPaths() {
        for (int i = absy - Enviro.width; i < absy + Enviro.width; i++) {
            for (int j = absx - enviro.getWidth (); j < absx + enviro.getWidth (); j++) {
                paths[i][j] = Double.MAX_VALUE;
            }
        }
        ArrayList<Cell> visited = new ArrayList<Cell> ();
        ArrayList<Cell> open = new ArrayList<Cell> ();
        open.add (cell);
        paths[absy][absx] = 0;
        while (!open.isEmpty ()) {
            Cell min = null;
            double minValue = 99999;
            for (int i = 0; i < open.size (); i++) {
                Cell next = open.get (i);
                if (paths[next.getAbsY ()][next.getAbsX ()] < minValue) {
                    min = next;
                    minValue = paths[next.getAbsX ()][next.getAbsY ()];
                }
            }
            int x = min.getAbsX ();
            int y = min.getAbsY ();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0) && Math.abs (i + j) == 1 && Math.abs (absx - x - j) <= range && Math.abs (absy - y - i) <= range) {
                        Cell next = world.getAbsCell (j + x, i + y);
                        if (!visited.contains (next) && !open.contains (next)) {
                            if (paths[i + y][j + x] == -1 || paths[y][x] + wMap.getWeight (j + x, i + y) < paths[i + y][j + x]) {
                                paths[i + y][j + x] = paths[y][x] + wMap.getWeight (j + x, i + y);
                            }
                            open.add (next);
                        }
                    }
                }
            }
            visited.add (min);
            open.remove (min);
        }
        return paths;
    }

    public double calcWeight(Cell cell) {
        return 1;
    }

    public Enviro getEnviro() {
        return enviro;
    }

    public void setEnviro(Enviro enviro) {
        this.enviro = enviro;
    }

    public GenCode getCode() {
        return code;
    }

    public void setCode(GenCode code) {
        this.code = code;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public double getThirst() {
        return thirst;
    }

    public void setThirst(double thirst) {
        this.thirst = thirst;
    }

    public double getMaxThirst() {
        return maxThirst;
    }

    public void setMaxThirst(double maxThirst) {
        this.maxThirst = maxThirst;
    }

    public double getHunger() {
        return hunger;
    }

    public void setHunger(double hunger) {
        this.hunger = hunger;
    }

    public double getMaxHunger() {
        return maxHunger;
    }

    public void setMaxHunger(double maxHunger) {
        this.maxHunger = maxHunger;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public double getMovementProgress() {
        return movementProgress;
    }

    public void setMovementProgress(double movementProgress) {
        this.movementProgress = movementProgress;
    }

    public Critter getMate() {
        return mate;
    }

    public void setMate(Critter mate) {
        this.mate = mate;
    }

    public int getMateTolerance() {
        return mateTolerance;
    }

    public void setMateTolerance(int mateTolerance) {
        this.mateTolerance = mateTolerance;
    }

    public int getAbsx() {
        return absx;
    }

    public void setAbsx(int absx) {
        this.absx = absx;
    }

    public int getAbsy() {
        return absy;
    }

    public void setAbsy(int absy) {
        this.absy = absy;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
