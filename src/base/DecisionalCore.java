package base;

import baseCells.FreshWater;
import baseCells.Solid;
import enumLists.CellList;

import java.util.*;

public class DecisionalCore {
    public static double[][] weights;
    Critter owner;
    Critter interacting;
    String behaviour = "";
    ArrayDeque<int[]> path;
    String action;
    private final Map<String, Float> propensionMap = new HashMap<> ();
    private final Map<String, Float> crossMap = new HashMap<> ();
    private int wanderX = 0;
    private int wanderY = 0;

    public DecisionalCore(Critter c) {
        this.owner = c;
        BitSet set = owner.getCode ().getGene ("PropensionCluster");
        int index = 0;
        for (CellList row : CellList.values ()) {
            this.propensionMap.put (row.name (), (float) Critter.shiftToRange (0.2, 2, (int) GenCode.convert (set.get (index * 8, index * 8 + 8)), 256));
            index++;
        }
        set = owner.getCode ().getGene ("CrossingCluster");
        index = 0;
        for (CellList row : CellList.values ()) {
            this.crossMap.put (row.name (), (float) Critter.shiftToRange (0.2, 2, (int) GenCode.convert (set.get (index * 8, index * 8 + 8)), 256));
            index++;
        }
    }

    public String act() {
        String action = "";
        switch (behaviour) {
            case "seek":
                seek ();
                break;
            case "flee":
                flee ();
                break;
            case "waitMate":
                waitMate ();
                break;
            case "seekMate":
                seekMate ();
                break;
            default: {
                if (owner.getSize () <= 2 && owner.getHunger () < owner.getMaxHunger () / 3) {
                    owner.split ();
                } else {
                    if (owner.getSize () <= 1) {
                        if (owner.getWorld ().getR ().nextInt (100) < 20) {
                            owner.moveTo (owner.getAbsx () + owner.getWorld ().getR ().nextInt (3) - 1, owner.getAbsy () + owner.getWorld ().getR ().nextInt (3) - 1);
                        } else {
                            if (owner.getEnviro ().getHumidity () > 60 || owner.getEnviro ().getBiome ().equals ("Ocean")) {
                                owner.eatSun ();
                            }
                        }
                        owner.setThirst (0);
                    } else {
                        if (owner.getAge () > 80 && lookForMate ()) {
                            action += "found mate ";
                        }
                        if (path != null && !path.isEmpty ()) {
                            action += "stepping ";
                            owner.setMovementProgress (owner.getMovementProgress () + owner.getSpeed ());
                            while (owner.getMovementProgress () > 1 && !path.isEmpty ()) {
                                owner.setMovementProgress (owner.getMovementProgress () - (1 / owner.getSpeed ()));
                                int[] next = path.removeFirst ();
                                owner.moveTo (next[0], next[1]);
                                ((Solid) owner.getWorld ().getAbsCell (next[0], next[1])).onPassage (owner);
                            }
                        } else { //PRIORITA FOTTUTE DA REWORKARE
                            if (owner.getCell ().getFoods () != null) { //EATONCELL RETURNS TRUE IF THE CRITTER ATE
                                owner.eatOnCell ();
                                action += "eating ";
                            } else {
                                if (owner.getCell () instanceof FreshWater && owner.getThirst () > 5) {
                                    action += "drinking ";
                                    owner.drinkOnCell ();
                                }
                            }
                            if (owner.getHunger () > 20 && owner.getWorld ().getR ().nextInt (150) < owner.getAggressiveness () && hunt ()) {
                                action += "hunting ";
                            } else { // PRIORITY 7
                                action += "pathing ";
                                path = choosePath ();
                            }
                        }
                    }
                }

            }
        }
        return action;
    }


    public ArrayDeque<int[]> choosePath() {
        weights = dijkPaths ();
        int range = owner.getRange ();
        World world = owner.getWorld ();
        int absx = owner.getAbsx ();
        int absy = owner.getAbsy ();
        int dietType = owner.getDietType ();

        int destx = absx, desty = absy;
        double max = Double.MIN_VALUE;
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                Cell c = world.getAbsCell (j + absx, i + absy);
                if (c != null) {
                    double propension = this.propensionMap.get (c.getType ());
                    if (c.getFoods () != null) {
                        int amount = 0;
                        for (int k = 0; k < c.getFoodTypes ().size (); k++) {
                            if (c.getFoodTypes ().get (k) == dietType) {
                                amount += c.getFoodAmount (k) * 1;
                            } else {
                                if (c.getFoodTypes ().get (k) + 1 == dietType || c.getFoodTypes ().get (k) - 1 == dietType) {
                                    amount += c.getFoodAmount (k) * 0.7;
                                }
                            }
                        }
                        propension += (owner.getHunger ()) / 2 + amount / 70;
                    }
                    if (c instanceof FreshWater) {
                        propension += owner.getThirst ();
                    }
                    propension -= weights[i + absy][j + absx];
                    if (propension > max) {
                        max = propension;
                        destx = j + absx;
                        desty = i + absy;
                    }
                }
            }
            owner.getActions ().add ("M: " + absx + " " + destx + " " + absy + " " + desty);
        }
        if (destx == absx && desty == absy) { //WANDER
            int heartRate = 0;
            do {
                heartRate++;
                if (heartRate == 20) { //100% SCIENTIFIC, NOT HERE TO PREVENT INFINITE LOOPS
                    owner.setAlive (false);
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
            } while (wanderX == 0 && wanderY == 0);
        } else {
            wanderX = 0;
            wanderY = 0;
        }
        return pathTo (destx, desty);
    }

    public ArrayDeque<int[]> pathTo(int destx, int desty) {
        int absx = owner.getAbsx ();
        int absy = owner.getAbsy ();
        path = new ArrayDeque<> ();

        if (absx == destx && absy == desty) {
            return null;
        }
        double min = Double.MAX_VALUE;
        int nextx = 0, nexty = 0;
        while (!(desty == absy && destx == absx)) {
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
        int absx = owner.getAbsx ();
        int absy = owner.getAbsy ();
        World world = owner.getWorld ();
        int range = owner.getRange ();

        for (int i = absy - Enviro.width * 2; i < absy + Enviro.width * 2; i++) {
            for (int j = absx - Enviro.width * 2; j < absx + Enviro.width * 2; j++) {
                if (i >= 0 && j >= 0 && i < world.getFullWidth () && j < world.getFullHeight ())
                    weights[i][j] = Double.MAX_VALUE;
            }
        }
        ArrayList<Cell> visited = new ArrayList<Cell> ();
        ArrayList<Cell> open = new ArrayList<Cell> ();
        open.add (owner.getCell ());
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

    public void flee() {
        int dist = Math.abs (owner.getAbsx () - interacting.getAbsx ()) + Math.abs (owner.getAbsx () - interacting.getAbsx ());
        if (dist > owner.getRange () + 1 || !interacting.isAlive ()) {
            behaviour = "";
            interacting = null;
        } else {
            stepFlee (interacting);
        }
    }

    public void seek() {
        int dist = Math.abs (owner.getAbsx () - interacting.getAbsx ()) + Math.abs (owner.getAbsx () - interacting.getAbsx ());
        if (dist > owner.getRange () + 1 || !interacting.isAlive ()) {
            behaviour = "";
            interacting = null;
        } else {
            action += "SEEK ";
            if (dist == 0) {
                action += "FAITO ";
                Random r = owner.getWorld ().getR ();
                double gauss = (r.nextGaussian () / 4) + 0.5;
                if (gauss * owner.getDietType () * owner.getSize () < (1 - gauss) * r.nextDouble () * interacting.getDietType () * interacting.getSize ()) {  //HIGH DEPTH STRATEGICAL COMBAT
                    owner.setAlive (false);
                } else {
                    interacting.setAlive (false);
                    interacting = null;
                    behaviour = "";
                }
                Critter.kDeaths++;
            } else {
                stepSeek (interacting);
                interacting.triggerPerception (owner);
            }
        }
    }

    public void seekMate() {
        action += "SEEK MATE";
        stepSeek (interacting);
        if (interacting.isAlive ()) {
            if (interacting.getAbsx () == owner.getAbsx () && interacting.getAbsy () == owner.getAbsy ()) {
                action += "FUCKED ";
                interacting = null;
                behaviour = "";
            }
        } else {
            interacting = null;
            behaviour = "";
        }
    }

    public void waitMate() {
        action += "WAIT MATE ";
        if (interacting.isAlive ()) {
            if (interacting.getAbsx () == owner.getAbsx () && interacting.getAbsy () == owner.getAbsy ()) {
                action += "REPRODUCE ";
                owner.reproduce (interacting);
                interacting = null;
                behaviour = "";
            }
        } else {
            interacting = null;
            behaviour = "";
        }
    }

    public void stepFlee(Critter other) {
        owner.setMovementProgress (owner.getMovementProgress () + owner.getSpeed ());
        while (owner.getMovementProgress () > 1) {
            owner.setMovementProgress (owner.getMovementProgress () - (1 / owner.getSpeed ()));
            int destx = owner.getAbsx () + (int) Math.signum (owner.getAbsx () - other.getAbsx ());
            int desty = owner.getAbsy () + (int) Math.signum (owner.getAbsy () - other.getAbsy ());
            if (owner.getWorld ().getAbsCell (destx, desty) != null) {
                owner.moveTo (destx, desty);
                ((Solid) owner.getWorld ().getAbsCell (destx, desty)).onPassage (owner);
            }
        }
    }

    public void stepSeek(Critter other) {
        owner.setMovementProgress (owner.getMovementProgress () + owner.getSpeed ());
        while (owner.getMovementProgress () > 1 && !(owner.getAbsx () == other.getAbsx () && owner.getAbsy () == other.getAbsy ())) {
            owner.setMovementProgress (owner.getMovementProgress () - (1 / owner.getSpeed ()));
            int destx = owner.getAbsx () + (int) Math.signum (-owner.getAbsx () + other.getAbsx ());
            int desty = owner.getAbsy () + (int) Math.signum (-owner.getAbsy () + other.getAbsy ());
            if (owner.getWorld ().getAbsCell (destx, desty) != null) {
                owner.moveTo (destx, desty);
                ((Solid) owner.getWorld ().getAbsCell (destx, desty)).onPassage (owner);
            }
        }
    }

    public boolean lookForMate() {
        Enviro enviro = owner.getEnviro ();
        World w = owner.getWorld ();
        for (int i = enviro.getY () - 1; i <= enviro.getY () + 1; i++) {
            for (int j = enviro.getX () - 1; j <= enviro.getX () + 1; j++) {
                if (j >= 0 && i >= 0 && j < w.getMap ().size () && i < w.getMap ().size ()) {
                    Enviro current = w.getMap ().get (enviro.getY ()).get (enviro.getX ());
                    for (Critter critter : current.getCritters ()) {
                        if (!critter.equals (owner) && critter.getSize () >= 3 && Math.abs (critter.getAbsx () - owner.getAbsx ()) < owner.getRange () - 1 && Math.abs (critter.getAbsy () - owner.getAbsy ()) < critter.getRange () - 1) {
                            int diff = critter.getCode ().getFullDiff (owner.getCode ());
                            if (diff < owner.getMateTolerance ()) {
                                if (critter.mateHandshake (owner, diff)) {
                                    this.interacting = critter;
                                    this.behaviour = "seekMate";
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean hunt() {
        Enviro enviro = owner.getEnviro ();
        World w = owner.getWorld ();
        for (int i = enviro.getY () - 1; i <= enviro.getY () + 1; i++) {
            for (int j = enviro.getX () - 1; j <= enviro.getX () + 1; j++) {
                if (j >= 0 && i >= 0 && j < w.getMap ().size () && i < w.getMap ().size ()) {
                    Enviro current = w.getMap ().get (enviro.getY ()).get (enviro.getX ());
                    for (Critter critter : current.getCritters ()) {
                        if (!critter.equals (owner) && Math.abs (critter.getAbsx () - owner.getAbsx ()) < owner.getRange () - 1 && Math.abs (critter.getAbsy () - owner.getAbsy ()) < critter.getRange () - 1) {
                            this.interacting = critter;
                            this.behaviour = "seek";
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Critter getInteracting() {
        return interacting;
    }

    public void setInteracting(Critter interacting) {
        this.interacting = interacting;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }
}
