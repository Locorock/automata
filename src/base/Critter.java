package base;

import baseCells.Food;
import baseCells.FreshWater;
import baseCells.Solid;
import enumLists.CellList;

import java.util.*;

public class Critter implements Comparable<Critter> {
    public static double[][] weights;
    private Enviro enviro;
    private GenCode code;
    public static long id = 0;
    public String name;
    private Cell cell;
    private int absx;
    private double thirst = 0;
    private int absy;
    private double hunger = 0;
    public ArrayDeque<int[]> path;
    public static int tDeaths = 0;
    public static int fDeaths = 0;
    public static int aDeaths = 0;
    private double baseSpeed;
    private double movementProgress = 0;
    private int maxThirst = 200;
    private int maxHunger = 200;
    private Map<String, Float> propensionMap = new HashMap<> ();
    private Map<String, Float> crossMap = new HashMap<> ();
    private int range = 8; //HARDCODED FOR NOW
    private Critter mate;
    private double speed = 2;
    private int mateTolerance;
    private double mateRate;
    private double foodEff;
    private double waterEff;
    private double height;
    private double webbedFeet;
    private int dietType;
    private int timeToLive;
    private boolean alive = true;
    private double age = 0;
    private int mateCooldown;
    private int mateElapsedTime = 0;
    private World world;
    private int wanderX = 0;
    private int wanderY = 0;
    private int lastWaterX;
    private int lastWaterY;
    private ArrayDeque actions = new ArrayDeque ();


    public Critter(String name, World w, int absx, int absy) {
        this (name, w, absx, absy, new GenCode (w.getR ()));
        this.age = 10;
        //EXPERIMENT
    }

    public Critter(String name, World w, int absx, int absy, Critter father, Critter mother) {
        this (name, w, absx, absy, new GenCode (father.getCode (), mother.getCode (), w.getR ()));
        this.hunger = 140;
        this.thirst = 140;
    }

    public Critter(String name, World w, int absx, int absy, GenCode code) {
        this.absx = absx;
        this.absy = absy;
        this.world = w;
        this.cell = world.getAbsCell (absx, absy);
        this.enviro = cell.getEnviro ();
        this.name = name + "_" + id++;
        this.code = code;
        this.buildTraits ();
        this.mateTolerance = 16;
    }

    public void buildTraits() {
        this.mateTolerance = code.getCardinality ("AppearanceTolerance");
        this.mateRate = shiftToRange (0.5, 2, code.getDecimal ("MateRate"), 256);
        this.foodEff = shiftToRange (1, 1, code.getDecimal ("FoodEff"), 256);
        this.waterEff = shiftToRange (1, 1, code.getDecimal ("WaterEff"), 256);
        this.baseSpeed = shiftToRange (0.5, 2, code.getDecimal ("BaseSpeed"), 256) + world.getR ().nextDouble () / 2 - 0.25;
        this.height = shiftToRange (0.5, 4, code.getDecimal ("Height"), 256);
        this.webbedFeet = shiftToRange (0, 3, code.getDecimal ("WebbedFeet"), 256);
        this.dietType = code.getCardinality ("DietType");

        BitSet set = this.code.getGene ("PropensionCluster");
        int index = 0;
        for (CellList row : CellList.values ()) {
            this.propensionMap.put (row.name (), (float) shiftToRange (0.2, 2, (int) GenCode.convert (set.get (index * 8, index * 8 + 8)), 256));
            index++;
        }
        set = this.code.getGene ("CrossingCluster");
        index = 0;
        for (CellList row : CellList.values ()) {
            this.crossMap.put (row.name (), (float) shiftToRange (0.2, 2, (int) GenCode.convert (set.get (index * 8, index * 8 + 8)), 256));
            index++;
        }

        this.timeToLive = (int) (300 + (world.getR ().nextInt (50)) - (mateRate * 80));
        this.mateCooldown = (int) (20 * mateRate * 2);
    }

    public void tick() {
        //AGING AND STATUS EFFECTS
        //System.out.println (name + " / " + (int)(hunger) + " / " + (int)(thirst));
        String action = "";
        if (this.thirst > this.maxThirst || this.hunger > this.maxHunger || age > timeToLive) {

            this.alive = false;
            if (this.thirst > this.maxThirst) {
                tDeaths++;
            }
            if (this.hunger > this.maxHunger) {
                fDeaths++;
            }
            if (this.age > this.timeToLive) {
                aDeaths++;
            }
            action += "Ded";
            return;
        } else {
            this.thirst += 0.1 * foodEff;
            this.hunger += 0.05 * waterEff;
            this.age += 0.1;
            if (mateElapsedTime > 0) {
                mateElapsedTime--;
            }
        }
        if (cell instanceof Food && this.propensionMap.get (cell.getType ()) * this.hunger > 5 && eatOnCell () && (path == null || path.isEmpty ())) {
            action += "Ate stuff " + this.propensionMap.get (cell.getType ()) * this.hunger + " ";
        } else {
            if (cell instanceof FreshWater && this.propensionMap.get (cell.getType ()) * this.thirst > 5 && (path == null || path.isEmpty ())) {
                drinkOnCell ();
                action += "Drunk " + this.propensionMap.get (cell.getType ()) * this.thirst + " ";
            } else {
                if (mate != null) {
                    if (mate.getAbsx () == absx && mate.getAbsy () == absy) {
                        reproduce (mate);
                        action += "Reproduced ";
                    }
                } else {
                    if (this.age > 80) {
                        //System.out.println ("looking");
                        lookForMate ();
                        action += "Looked for mateys ";
                    }
                    if (path == null || path.isEmpty ()) {
                        path = choosePath ();
                        action += "Big thinking ";
                    } else {
                        //MOVEMENT COST
                        movementProgress += speed;
                        action += "Moving";
                        while (movementProgress > 1 && !path.isEmpty ()) {
                            movementProgress -= (1 / speed);
                            int[] next = path.removeFirst ();
                            moveTo (next[0], next[1]);
                            ((Solid) world.getAbsCell (next[0], next[1])).onPassage (this);
                            action += "Moved to " + next[0] + "/" + next[1] + " ";
                        }
                        this.hunger += 0.1 * foodEff * baseSpeed;
                        this.thirst += 0.05 * waterEff * baseSpeed;
                    }
                }
            }
        }
        actions.add (action);
        if (actions.size () > 40) {
            actions.removeFirst ();
        }
    }

    public void lookForMateOld() {
        for (Critter critter : world.getCritters ()) {
            if (Math.abs (critter.absx - this.absx) < range && Math.abs (critter.absy - this.absy) < range) {
                int diff = critter.code.getHammingDiff ("AppearanceCluster", this.code.getGene ("AppearanceCluster"));
                if (diff < mateTolerance) {
                    if (critter.mateHandshake (this, diff)) {
                        weights = dijkPaths ();
                        path = pathTo (critter.getAbsx (), critter.getAbsy ());
                    }
                }
            }
        }
    }

    public void lookForMate() {
        for (int i = enviro.getY () - 1; i <= enviro.getY () + 1; i++) {
            for (int j = enviro.getX () - 1; j <= enviro.getX () + 1; j++) {
                if (j >= 0 && i >= 0 && j < world.getMap ().size () && i < world.getMap ().size ()) {
                    Enviro current = world.getMap ().get (enviro.getY ()).get (enviro.getX ());
                    for (Critter critter : current.getCritters ()) {
                        if (!critter.equals (this) && Math.abs (critter.getAbsx () - this.absx) < range - 1 && Math.abs (critter.getAbsy () - this.absy) < range - 1) {
                            int diff = critter.code.getHammingDiff ("AppearanceCluster", this.code.getGene ("AppearanceCluster"));
                            if (diff < mateTolerance) {
                                if (critter.mateHandshake (this, diff)) {
                                    weights = dijkPaths ();
                                    path = pathTo (critter.getAbsx (), critter.getAbsy ());
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    public boolean mateHandshake(Critter critter, int diff) {
        if (diff < mateTolerance) {
            if (this.hunger < this.maxHunger / 3 && this.thirst < this.maxThirst / 3 && this.mateElapsedTime == 0) {
                this.mate = critter;
                return true;
            }
        }
        return false;
    }

    public void reproduce(Critter father) {
        Critter child = new Critter ("Salino", world, absx, absy, father, this);
        world.getCritters ().add (child);
        this.enviro.getCritters ().add (child);
        this.mateElapsedTime = mateCooldown;
        this.hunger += 35;
        this.thirst += 35;
        this.mate = null;
    }

    public ArrayDeque<int[]> choosePath() {
        weights = dijkPaths ();
        int destx = 0, desty = 0;
        double max = Double.MIN_VALUE;
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                //System.out.println ("Premod propension: "+propension);
                Cell c = world.getAbsCell (j + absx, i + absy);
                if (c != null) {
                    double propension = this.propensionMap.get (c.getType ()) + world.getR ().nextGaussian ();
                    if (c instanceof Food) {
                        Food f = ((Food) c);
                        int amount = 0;
                        for (int k = 0; k < f.getFoodTypes ().size (); k++) {
                            if (f.getFoodTypes ().get (k) == dietType) {
                                amount += f.getFoodAmount (k) * 1;
                            } else {
                                if (f.getFoodTypes ().get (k) + 1 == dietType || f.getFoodTypes ().get (k) - 1 == dietType) {
                                    amount += f.getFoodAmount (k) * 0.7;
                                }
                            }
                        }
                        propension += (hunger) / 2 + amount / 70; //test
                    }
                    if (c instanceof FreshWater) {
                        propension += thirst;
                    }
                    //System.out.println ("Postmod propension: "+propension);
                    if (propension > max) {
                        max = propension;
                        destx = j + absx;
                        desty = i + absy;
                    }
                }
            }
        }
        if (destx == absx && desty == absy) { //WANDER
            System.out.println ("wander");
            int heartRate = 0;
            do {
                heartRate++;
                if (heartRate == 20) { //100% SCIENTIFIC, NOT HERE TO PREVENT INFINITE LOOPS
                    this.alive = false;
                    break;
                }
                if (wanderX == 0 && wanderY == 0) { //PUNTO SUI LIMITI DEL RANGE
                    int direction = world.getR ().nextInt (4);
                    if (direction >= 2) {
                        wanderX = world.getR ().nextInt (range * 2 + 1) - range;
                        if (direction == 2) {
                            wanderY = 0;
                        } else {
                            wanderY = range;
                        }
                    } else {
                        wanderY = world.getR ().nextInt (range * 2 + 1) - range;
                        if (direction == 0) {
                            wanderX = 0;
                        } else {
                            wanderX = range;
                        }
                    }
                }
                destx = absx + wanderX;
                desty = absy + wanderY;
                /*
                if (world.getAbsCell (destx, desty) instanceof SaltWater || world.getAbsCell (destx, desty) instanceof RiverWater) {
                    wanderX = 0;
                    wanderY = 0;
                }
                */
            } while (wanderX == 0 && wanderY == 0);
        } else {
            wanderX = 0;
            wanderY = 0;
        }
        return pathTo (destx, desty);
    }

    public ArrayDeque<int[]> pathTo(int destx, int desty) {
        path = new ArrayDeque<> ();
        if (absx == destx && absy == desty) {
            return null;
        }
        double min = Double.MAX_VALUE;
        int nextx = 0, nexty = 0;
        while (!(desty == this.absy && destx == this.absx)) {
            path.addFirst (new int[]{destx, desty});
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0) && Math.abs (i + j) == 1) {
                        try {
                            if (weights[i + desty][j + destx] < min) {
                                min = weights[i + desty][j + destx];
                                nextx = j + destx;
                                nexty = i + desty;

                            }
                        } catch (Exception e) {
                            return null;
                        }

                    }
                }
            }
            destx = nextx;
            desty = nexty;
        }

        return path;
    }

    public double[][] dijkPaths() {
        for (int i = absy - Enviro.width * 2; i < absy + Enviro.width * 2; i++) {
            for (int j = absx - Enviro.width * 2; j < absx + Enviro.width * 2; j++) {
                if (i >= 0 && j >= 0 && i < world.getFullWidth () && j < world.getFullHeight ())
                    weights[i][j] = Double.MAX_VALUE;
            }
        }
        ArrayList<Cell> visited = new ArrayList<Cell> ();
        ArrayList<Cell> open = new ArrayList<Cell> ();
        open.add (cell);
        weights[absy][absx] = 0;
        while (!open.isEmpty ()) {
            Cell min = null;
            double minValue = 99999;
            for (int i = 0; i < open.size (); i++) {
                Cell next = open.get (i);
                if (weights[next.getAbsY ()][next.getAbsX ()] < minValue) {
                    min = next;
                    minValue = weights[next.getAbsY ()][next.getAbsX ()];
                }
            }
            int x = min.getAbsX ();
            int y = min.getAbsY ();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0) && Math.abs (i + j) == 1 && Math.abs (absx - x) + j <= range && Math.abs (absy - y) + i <= range) {
                        Cell next = world.getAbsCell (j + x, i + y);
                        if (next != null && !visited.contains (next) && !open.contains (next)) {
                            if (weights[i + y][j + x] == -1 || weights[y][x] + crossMap.get (next.getType ()) < weights[i + y][j + x]) {
                                weights[i + y][j + x] = weights[y][x] + crossMap.get (next.getType ());
                            }
                            open.add (next);
                        }
                    }
                }
            }
            visited.add (min);
            open.remove (min);
        }
        return weights;
    }

    public boolean eatOnCell() {
        Food food = ((Food) cell);
        double maxAmount = 0;
        double maxMult = 0;
        int max = -1;
        for (int i = 0; i < food.getFoodTypes ().size (); i++) {
            int type = food.getFoodTypes ().get (i);
            double mult = 1;
            if (dietType != type && type != 8) {
                if (dietType + 1 == type || dietType - 1 == type) {
                    mult = 0.7;
                } else {
                    mult = 0;
                }
            }
            if (mult * food.getFoodAmount (i) > maxAmount) {
                maxMult = mult;
                maxAmount = mult * food.getFoodAmount (i);
                max = i;
            }
        }
        if (max != -1) {
            double amount = food.onEat (this, max) * maxMult;
            if (amount * hunger > 5) {
                this.setHunger (this.getHunger () - amount);
                return true;
            }
        }
        return false;
    }

    public void drinkOnCell() {
        ((FreshWater) this.cell).onDrink (this);
    }

    public void moveTo(int absx, int absy) {
        this.cell = world.getAbsCell (absx, absy);
        this.absx = absx;
        this.absy = absy;
        this.enviro.getCritters ().remove (this);
        this.enviro = cell.getEnviro ();
        this.enviro.getCritters ().add (this);
    }

    public double shiftToRange(double rangeStart, double rangeEnd, int value, int subdivisions) {
        return (((rangeEnd - rangeStart) * value) / subdivisions) + rangeStart;
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

    public int getMaxThirst() {
        return maxThirst;
    }

    public void setMaxThirst(int maxThirst) {
        this.maxThirst = maxThirst;
    }

    public double getHunger() {
        return hunger;
    }

    public void setHunger(double hunger) {
        this.hunger = hunger;
    }

    public int getMaxHunger() {
        return maxHunger;
    }

    public void setMaxHunger(int maxHunger) {
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

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getMateRate() {
        return mateRate;
    }

    public void setMateRate(double mateRate) {
        this.mateRate = mateRate;
    }

    public double getFoodEff() {
        return foodEff;
    }

    public void setFoodEff(double foodEff) {
        this.foodEff = foodEff;
    }

    public double getWaterEff() {
        return waterEff;
    }

    public void setWaterEff(double waterEff) {
        this.waterEff = waterEff;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getDietType() {
        return dietType;
    }

    public void setDietType(int dietType) {
        this.dietType = dietType;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public ArrayDeque getActions() {
        return actions;
    }

    public void setActions(ArrayDeque actions) {
        this.actions = actions;
    }

    public double getWebbedFeet() {
        return webbedFeet;
    }

    public void setWebbedFeet(double webbedFeet) {
        this.webbedFeet = webbedFeet;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public int compareTo(Critter critter) {
        if (this.baseSpeed > critter.baseSpeed) {
            return 1;
        } else {
            if (this.baseSpeed == critter.baseSpeed) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
