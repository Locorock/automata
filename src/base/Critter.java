package base;

import baseCells.Food;
import baseCells.FreshWater;
import baseCells.Solid;
import cells.RiverWater;
import cells.SaltWater;
import enumLists.CellList;

import java.util.*;

public class Critter {
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
    private int maxThirst = 100;
    private int maxHunger = 100;
    private Map<String, Float> propensionMap = new HashMap<> ();
    private Map<String, Float> crossPropension = new HashMap<> ();
    private int range = 8; //HARDCODED FOR NOW
    private Critter mate;
    private double speed = 2;
    private int mateTolerance;
    private double mateRate;
    private double foodEff;
    private double waterEff;
    private double height;
    private int dietType;
    private int timeToLive;
    private boolean alive = true;
    private double age = 0;
    private int mateCooldown;
    private int mateElapsedTime = 0;
    private World world;
    private int gender;
    private int wanderX = 0;
    private int wanderY = 0;
    private int wander = 10;
    private int lastWaterX;
    private int lastWaterY;
    private ArrayDeque actions = new ArrayDeque ();


    public Critter(String name, World w, int absx, int absy) {
        this (name, w, absx, absy, new GenCode (w.getR ()));
        this.mateTolerance = 16;
        this.age = 10;
    }

    public Critter(String name, World w, int absx, int absy, Critter father, Critter mother) {
        this (name, w, absx, absy, new GenCode (father.getCode (), mother.getCode (), w.getR ()));
        this.hunger = 40;
        this.thirst = 40;
    }

    public Critter(String name, World w, int absx, int absy, GenCode code) {
        this.absx = absx;
        this.absy = absy;
        this.world = w;
        this.cell = world.getAbsCell (absx, absy);
        this.enviro = cell.getEnviro ();
        this.name = name + "_" + id++;
        this.code = code;
        this.gender = w.getR ().nextInt (2);
        this.mateTolerance = code.getCardinality ("AppearanceTolerance");
        this.mateRate = shiftToRange (0.5, 3, code.getDecimal ("MateRate"), 256);
        this.foodEff = shiftToRange (1, 1, code.getDecimal ("FoodEff"), 256);
        this.waterEff = shiftToRange (1, 1, code.getDecimal ("WaterEff"), 256);
        this.baseSpeed = shiftToRange (0.5, 2, code.getDecimal ("BaseSpeed"), 256);
        this.height = shiftToRange (0.5, 4, code.getDecimal ("Height"), 256);
        this.dietType = code.getCardinality ("DietType");
        this.timeToLive = (int) (300 + (w.getR ().nextInt (50)) - (mateRate * 80));
        this.mateCooldown = (int) (20 * mateRate * 2);
        BitSet set = this.code.getGene ("PropensionCluster");
        int index = 0;
        for (CellList row : CellList.values ()) {
            this.propensionMap.put (row.name (), (float) shiftToRange (0.1, 4, (int) GenCode.convert (set.get (index * 8, index * 8 + 8)), 256));
            index++;
        }
        set = this.code.getGene ("CrossingCluster");
        index = 0;
        for (CellList row : CellList.values ()) {
            this.crossPropension.put (row.name (), (float) shiftToRange (0.1, 4, (int) GenCode.convert (set.get (index * 8, index * 8 + 8)), 256));
            index++;
        }
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
        }
        if (mateElapsedTime > 0) {
            mateElapsedTime--;
        }
        if (mate != null) {
            if (mate.getAbsx () == absx && mate.getAbsy () == absy) {
                if (gender == 0) {
                    reproduce (mate);
                    action += "Reproduced";
                }
            }
            return;
        }
        if (path == null || path.isEmpty ()) {
            if (cell instanceof Food && this.propensionMap.get (cell.getType ()) * this.hunger > 5) {
                Food food = (Food) cell;
                if (eatOnCell ()) {
                    action += "Ate stuff";
                    return;
                }
                action += "Didn't eat stuff";
            }
            if (cell instanceof FreshWater && this.propensionMap.get (cell.getType ()) * this.thirst > 10) {
                //System.out.println ("Sippin that stuff");
                drinkOnCell ();
                action += "Drunk";
                return;
            }
            if (this.age > 40 && this.gender == 1) {
                //System.out.println ("looking");
                path = lookForMate ();
                action += "Looked for mateys";
            }
            if (path == null || path.isEmpty ()) {
                path = choosePath ();
                action += "Big thinking" + path.toString ();
            }
        } else {
            //MOVEMENT COST
            movementProgress += speed;
            //System.out.println ("Steppy steps");
            while (movementProgress > 1 && !path.isEmpty ()) {
                movementProgress -= (1 / speed);
                int[] next = path.removeFirst ();
                moveTo (next[0], next[1]);
                ((Solid) world.getAbsCell (next[0], next[1])).onPassage (this);
                action += "Moved to " + next[0] + "/" + next[1];
            }
            this.hunger += 0.1 * foodEff * baseSpeed;
            this.thirst += 0.05 * waterEff * baseSpeed;
            actions.add (action);
            if (actions.size () > 20) {
                actions.removeFirst ();
            }
        }
    }

    public ArrayDeque<int[]> lookForMate() {
        for (Critter critter : world.getCritters ()) {
            if (Math.abs (critter.absx - this.absx) < range && Math.abs (critter.absy - this.absy) < range && critter.getGender () == 0) {
                int diff = critter.code.getHammingDiff ("AppearanceCluster", this.code.getGene ("AppearanceCluster"));
                if (diff < mateTolerance) {
                    if (critter.mateHandshake (this, diff)) {
                        weights = dijkPaths ();
                        return pathTo (critter.getAbsx (), critter.getAbsy ());
                    }
                }
            }
        }
        return null;
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
        this.mateElapsedTime = mateCooldown;
        this.hunger += 20;
        this.thirst += 20;
        this.mate = null;
    }

    public ArrayDeque<int[]> choosePath() {
        weights = dijkPaths ();
        int destx = 0, desty = 0;
        double max = Double.MIN_VALUE;
        for (int i = -range; i < range; i++) {
            for (int j = -range; j < range; j++) {
                //System.out.println ("Premod propension: "+propension);
                Cell c = world.getAbsCell (j + absx, i + absy);
                double propension = this.propensionMap.get (c.getType ());
                if (c instanceof Food) {
                    propension += hunger * ((Food) c).getFoodAmount (0) / 70;
                }
                if (c instanceof FreshWater) {
                    propension += thirst / 2;
                }
                if (c instanceof SaltWater) {
                    propension -= 200;
                }
                //System.out.println ("Postmod propension: "+propension);
                if (propension > max) {
                    max = propension;
                    destx = j + absx;
                    desty = i + absy;
                }
            }
        }
        if (destx == absx && desty == absy) { //WANDER
            if (wander == 0) {
                wander = 10;
            }
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
                if (world.getAbsCell (destx, desty) instanceof SaltWater || world.getAbsCell (destx, desty) instanceof RiverWater) {
                    wanderX = 0;
                    wanderY = 0;
                }
            } while (wanderX == 0 && wanderY == 0);
        } else {
            wander--;
            if (wander == 0) {
                wanderX = 0;
                wanderY = 0;
            }
        }
        return pathTo (destx, desty);
    }

    public ArrayDeque<int[]> pathTo(int destx, int desty) {
        if (absx == destx && absy == desty) {
            return null;
        }
        ArrayDeque<int[]> path = new ArrayDeque<int[]> ();
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
                            this.alive = false;
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
        for (int i = absy - Enviro.width; i < absy + Enviro.width; i++) {
            for (int j = absx - Enviro.width; j < absx + Enviro.width; j++) {
                if (i >= 0 && j >= 0)
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
                    if (!(i == 0 && j == 0) && Math.abs (i + j) == 1 && Math.abs (absx - x - j) <= range && Math.abs (absy - y - i) <= range) {
                        Cell next = world.getAbsCell (j + x, i + y);

                        if (!visited.contains (next) && !open.contains (next)) {
                            if (weights[i + y][j + x] == -1 || weights[y][x] + crossPropension.get (next.getType ()) < weights[i + y][j + x]) {
                                weights[i + y][j + x] = weights[y][x] + crossPropension.get (next.getType ());
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
                    mult = 0.5;
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
        this.enviro = cell.getEnviro ();
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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
}
