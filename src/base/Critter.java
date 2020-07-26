package base;

import baseCells.FreshWater;
import enumLists.CellList;

import java.util.ArrayDeque;
import java.util.BitSet;

public class Critter implements Comparable<Critter> {
    public static long id = 0;
    public static int tDeaths = 0;
    public static int fDeaths = 0;
    public static int aDeaths = 0;
    public static int kDeaths = 0;
    public String name;
    public ArrayDeque<int[]> path;
    private Enviro enviro;
    private GenCode code;
    private Cell cell;
    private int absx;
    private double thirst = 0;
    private int absy;
    private double hunger = 0;
    private double baseSpeed;
    private double movementProgress = 0;
    private final DecisionalCore decisionalCore;
    private double maxThirst = 100;
    private int range = 8; //HARDCODED FOR NOW
    private double speed = 2;
    private int mateTolerance;
    private double mateRate;
    private double foodEff;
    private double waterEff;
    private double height;
    private double maxHunger = 100;
    private double webbedFeet;
    private double aggressiveness;
    private boolean alerted;
    private int dietType;
    private int timeToLive;
    private double size;
    private boolean alive = true;
    private double age = 0;
    private int mateCooldown;
    private int mateElapsedTime = 0;
    private World world;
    private int lastWaterX;
    private int lastWaterY;
    private ArrayDeque actions = new ArrayDeque ();


    public Critter(String name, World w, int absx, int absy) {
        this (name, w, absx, absy, new GenCode (w.getR ()));
        this.age = 80;
        //EXPERIMENT
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
        this.decisionalCore = new DecisionalCore (this);
        this.buildTraits ();
        this.size++; //0-->1
        this.range = (int) Math.round (size) + 3;
        this.maxHunger = (int) (this.maxHunger * size);
        this.maxThirst = (int) (this.maxThirst * size);
        this.hunger = this.maxHunger / 3;
        this.thirst = this.maxThirst / 3;
    }

    public static double shiftToRange(double rangeStart, double rangeEnd, int value, int subdivisions) {
        return (((rangeEnd - rangeStart) * value) / subdivisions) + rangeStart;
    }

    public void buildTraits() {
        this.mateRate = shiftToRange (0.5, 2, code.getDecimal ("MateRate"), 256);
        this.foodEff = shiftToRange (1, 1, code.getDecimal ("FoodEff"), 256);
        this.waterEff = shiftToRange (1, 1, code.getDecimal ("WaterEff"), 256);
        this.baseSpeed = shiftToRange (0.5, 2, code.getDecimal ("BaseSpeed"), 256) + world.getR ().nextDouble () / 2 - 0.25;
        this.height = shiftToRange (0.5, 4, code.getDecimal ("Height"), 256);
        this.webbedFeet = shiftToRange (0, 3, code.getDecimal ("WebbedFeet"), 256);
        this.aggressiveness = shiftToRange (0, 10, code.getCardinality ("Aggressiveness"), 256);
        this.size = code.getCardinality ("Size");
        this.dietType = code.getCardinality ("DietType");
        this.timeToLive = (int) ((300 + (world.getR ().nextInt (50)) - (mateRate * 80)) * size);
        this.mateCooldown = (int) (45 * mateRate * 2);
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
            this.thirst += 0.1 * waterEff * size;
            this.hunger += 0.05 * foodEff * size;
            this.age += 0.1;
            if (mateElapsedTime > 0) {
                mateElapsedTime--;
            }
        }
        action += decisionalCore.act ();
        actions.add (action);
        if (actions.size () > 40) {
            actions.removeFirst ();
        }
    }

    public boolean mateHandshake(Critter critter, int diff) {
        if (diff < mateTolerance) {
            if (this.decisionalCore.getInteracting () == null && this.hunger < this.maxHunger / 3 && this.thirst < this.maxThirst / 3 && this.mateElapsedTime == 0) {
                decisionalCore.setInteracting (critter);
                decisionalCore.setBehaviour ("waitMate");
                return true;
            }
        }
        return false;
    }

    public boolean eatOnCell() {
        double maxAmount = 0;
        double maxMult = 0;
        int max = -1;
        for (int i = 0; i < cell.getFoodTypes ().size (); i++) {
            int type = cell.getFoodTypes ().get (i);
            double mult = 1;
            if (dietType != type && type != 8) {
                if (dietType + 1 == type || dietType - 1 == type) {
                    mult = 0.5; //mult vicino alla dieta
                } else {
                    mult = 0; //mult lontano dalla dieta
                }
            }
            if (mult * cell.getEatable (i) > maxAmount) {
                maxMult = mult;
                maxAmount = mult * cell.getEatable (i);
                max = i;
            }
        }
        if (max != -1) {   //MODFICATO, RICHIEDE PIÙ CIBO ALL'INCREMENTARE DELLA TAGLIA E MANGIA PIÙ CIBO AL TURNO ALL'INCREMENTARE DI QUEST'ULTIMA
            double amount = cell.getEatable (max);
            if ((amount * (hunger - (thirst / 2))) / size > (maxHunger / 50)) {
                this.setHunger (this.getHunger () - amount * maxMult * size);
                cell.removeFood (max, amount * size);
                cell.onEat (this);
                return true;
            }
        }
        return false;
    }

    public void drinkOnCell() {
        ((FreshWater) this.cell).onDrink (this);
    }


    public void reproduce(Critter father) {
        Critter child = new Critter ("Salino", world, absx, absy, father, this);
        world.getCritters ().add (child);
        this.enviro.getCritters ().add (child);
        this.mateElapsedTime = mateCooldown;
        this.hunger += this.maxHunger / 7;
        this.thirst += this.maxThirst / 7;
    }

    //CELLULAR FUNCTIONS

    public void split() {
        Critter child = new Critter ("Beehhh", world, absx, absy, this.code);
        child.getCode ().mutate (world.getR ());
        world.getCritters ().add (child);
        this.enviro.getCritters ().add (child);
        this.hunger += this.maxHunger / 2;
        this.thirst += this.maxThirst / 2;
    }

    public void eatSun() {
        Enviro e = this.getEnviro ();
        int shared = 0;
        for (Critter c : e.getCritters ()) {
            if (c.getSize () == this.getSize ()) {
                shared++;
            }
        }
        double amount = (1 - (shared * 0.3)) * this.size;
        if (amount < 0) {
            amount = 0;
        }
        this.hunger -= amount;
    }

    public void moveTo(int absx, int absy) {
        Cell c = world.getAbsCell (absx, absy);
        if (c != null) {
            this.cell = c;
            this.absx = absx;
            this.absy = absy;
            this.enviro.getCritters ().remove (this);
            this.enviro = cell.getEnviro ();
            this.enviro.getCritters ().add (this);
            this.hunger += 0.1 * foodEff * baseSpeed * size * 0.5;
            this.thirst += 0.05 * waterEff * baseSpeed * size * 0.5;
        }
    }

    public void triggerPerception(Critter other) {
        if (!alerted) {
            double speed = other.speed;
            double occ = CellList.valueOf (other.getCell ().getType ()).getOccultation ();
            double camo = 1;
            double chance = ((1.0 / speed) * occ * camo * 2.0);
            System.out.println (chance);
            int n = this.world.getR ().nextInt ((int) (chance * 100));
            if (n < 50) {
                this.alert (other);
            }
        }
    }

    public void alert(Critter other) {
        BitSet appear = other.code.getGene ("AppearanceCluster");
        int n = this.code.getHammingDiff ("AppearanceRecognition", appear);
        if (n > 6) {
            decisionalCore.setBehaviour ("flee");
            decisionalCore.setInteracting (other);
        } else {
            decisionalCore.setBehaviour ("seek");
            decisionalCore.setInteracting (other);
        }
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

    public void setMaxThirst(int maxThirst) {
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

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayDeque<int[]> getPath() {
        return path;
    }

    public void setPath(ArrayDeque<int[]> path) {
        this.path = path;
    }

    public int getMateCooldown() {
        return mateCooldown;
    }

    public void setMateCooldown(int mateCooldown) {
        this.mateCooldown = mateCooldown;
    }

    public int getMateElapsedTime() {
        return mateElapsedTime;
    }

    public void setMateElapsedTime(int mateElapsedTime) {
        this.mateElapsedTime = mateElapsedTime;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public double getAggressiveness() {
        return aggressiveness;
    }

    public void setAggressiveness(double aggressiveness) {
        this.aggressiveness = aggressiveness;
    }
}
