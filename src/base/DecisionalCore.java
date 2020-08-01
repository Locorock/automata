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
            this.propensionMap.put (row.name (), (float) Critter.shiftToRange (0, 4, (int) GenCode.convert (set.get (index * 8, index * 8 + 8)), 256));
            index++;
        }
        set = owner.getCode ().getGene ("CrossingCluster");
        index = 0;
        for (CellList row : CellList.values ()) {
            this.crossMap.put (row.name (), (float) Critter.shiftToRange (0, 4, (int) GenCode.convert (set.get (index * 8, index * 8 + 8)), 256));
            index++;
        }
    }

    public String act() {
        action = "";
        switch (behaviour) {
            case "seek":
                seek ();
                action += "seek ";
                break;
            case "flee":
                flee ();
                action += "flee ";
                break;
            case "waitMate":
                waitMate ();
                action += "wait ";
                break;
            case "seekMate":
                seekMate ();
                action += "semate ";
                break;
            default: {
                if (owner.getSize () <= 2 && owner.getHunger () < owner.getMaxHunger () / 3) {
                    owner.split ();
                    action += "split ";
                } else {
                    if (owner.getSize () <= 1) {
                        if (owner.getWorld ().getR ().nextInt (100) < 20) {
                            owner.moveTo (owner.getAbsx () + owner.getWorld ().getR ().nextInt (3) - 1, owner.getAbsy () + owner.getWorld ().getR ().nextInt (3) - 1);
                        } else {
                            if (owner.getEnviro ().getHumidity () > 60 || owner.getEnviro ().getBiome ().equals ("Ocean")) {
                                owner.eatSun ();
                                action += "sun ";
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
                                action += " | " + next[0] + " - " + next[1];
                                owner.moveTo (next[0], next[1]);
                                if (owner.getWorld ().getAbsCell (next[0], next[1]) != null) {
                                    ((Solid) owner.getWorld ().getAbsCell (next[0], next[1])).onPassage (owner);
                                }
                            }
                        } else { //PRIORITA FOTTUTE DA REWORKARE
                            double food = 0;
                            if (owner.getCell ().getFoods () != null) { //EATONCELL RETURNS TRUE IF THE CRITTER ATE
                                food = owner.eatOnCell ();
                                action += "eating ";
                            } else {
                                if (owner.getCell () instanceof FreshWater && owner.getThirst () > 10 * owner.getSize ()) {
                                    action += "drinking ";
                                    owner.drinkOnCell ();
                                    food = 40;
                                } else {
                                    if (owner.getHunger () > 20 && owner.getWorld ().getR ().nextInt (150) < owner.getAggressiveness () && hunt ()) {
                                        action += "hunting ";
                                        food = 40;
                                    }
                                }
                            }
                            if ((food * owner.getHunger () - owner.getThirst ()) / 2 < 30) {
                                action = "pathing ";
                                path = choosePath ();
                            }
                        }
                    }
                }

            }
        }
        return action;
    }

    public double estimateTravelCost(int[] point) { //UNWRAPPED
        //ArrayDeque<int[]> path = straightPath (point[0],point[1]);
        double lenght = 0;
        /*
        while(!path.isEmpty ()){
            int[] step = path.pop();
            Cell c = owner.getWorld ().getAbsCell (step[0], step[1]);
            lenght+=crossMap.get(c.getType ());
        },

         */
        return Math.abs (point[0] - owner.getAbsx ()) + Math.abs (point[1] - owner.getAbsy ());
    }

    public ArrayDeque<int[]> choosePath() {
        double start = System.nanoTime ();
        int range = owner.getRange ();
        World world = owner.getWorld ();
        int absx = owner.getAbsx ();
        int absy = owner.getAbsy ();
        int dietType = owner.getDietType ();

        int destx = absx, desty = absy;
        double max = -100; //MINIMUM WEIGHT
        double maxw = 0;
        double maxt = 0;
        double maxh = 0;
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                int[] coo = wrapCoordinates (new int[]{j + absx, i + absy});
                Cell c = world.getAbsCell (coo[0], coo[1]);
                if (c != null) {
                    double hunger = 0, thirst = 0, weight = 0;
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
                        propension += (owner.getHunger () / 2) * (amount / 2);
                        hunger = (owner.getHunger () / 2) * (amount / 2);
                    }
                    if (c instanceof FreshWater) {
                        propension += owner.getThirst ();
                        thirst = owner.getThirst ();
                    }
                    propension -= weights[coo[0]][coo[1]] * 2;
                    propension *= propensionMap.get (c.getType ());
                    weight = estimateTravelCost (new int[]{owner.getAbsx () + j, owner.getAbsy () + i});
                    if (propension > max) {
                        max = propension;
                        maxw = weight;
                        maxh = hunger;
                        maxt = thirst;
                        destx = j + absx;
                        desty = i + absy;
                    }
                }
            }
        }
        action += " | MAX " + max + " | WEIGHT " + maxw + " | FOOD " + maxh + " | THIRST " + maxt + " | ";
        if (max < 0) { //WANDER
            System.out.println ("wander");
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
            action += "WANDER TO " + destx + "-" + desty;
        }
        ArrayDeque<int[]> path = shitPathing (destx, desty);
        return path;
    }

    public String concat(int[] stoc) {
        return stoc[0] + "" + stoc[1];
    }

    public ArrayDeque<int[]> shitPathing(int destx, int desty) {
        double s = System.nanoTime ();
        int[] start = new int[]{owner.getAbsx (), owner.getAbsy ()};
        int[] goal = new int[]{destx, desty};
        ArrayDeque<int[]> openSet = new ArrayDeque<> ();
        openSet.add (start);
        HashMap<int[], int[]> cameFrom = new HashMap<> ();
        HashMap<String, Double> gScore = new HashMap<> ();
        gScore.put (concat (start), 0d);
        HashMap<String, Double> fScore = new HashMap<> ();
        fScore.put (concat (start), estimateTravelCost (start));
        int count = 0;
        while (!openSet.isEmpty ()) {
            int[] current = null;
            double min = Double.MAX_VALUE;
            for (int[] value : openSet) {
                double cur = fScore.get (concat (value));
                if (cur < min) {
                    min = cur;
                    current = value;
                }
            }
            count++;
            if (current[0] == goal[0] && current[1] == goal[1]) {
                return reconstructPath (cameFrom, wrapCoordinates (current));
            }

            openSet.remove (current);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int[] neighbor = new int[]{current[0] + i, current[1] + j};
                    if (Math.abs (j + i) == 1 && Math.abs (start[0] - neighbor[0]) <= owner.getRange () && Math.abs (start[1] - neighbor[1]) <= owner.getRange ()) {
                        int[] wrap = wrapCoordinates (neighbor);
                        Cell cCell = owner.getWorld ().getAbsCell (wrap[0], wrap[1]);
                        double tentative_gScore = gScore.get (concat (current)) + crossMap.get (cCell.getType ());
                        try {
                            if (gScore.get (concat (neighbor)) == null) {
                                gScore.put (concat (neighbor), Double.MAX_VALUE);
                            }
                        } catch (NullPointerException e) {
                            gScore.put (concat (neighbor), Double.MAX_VALUE);
                        }
                        if (tentative_gScore < gScore.get (concat (neighbor))) {
                            cameFrom.put (wrapCoordinates (neighbor), wrapCoordinates (current));
                            gScore.put (concat (neighbor), tentative_gScore);
                            fScore.put (concat (neighbor), gScore.get (concat (neighbor)) + estimateTravelCost (neighbor));
                            boolean found = false;
                            for (int[] value : openSet)
                                if (value[0] == neighbor[0] && value[1] == neighbor[1]) {
                                    found = true;
                                }
                            if (!found) {
                                openSet.add (neighbor);
                            }
                        }
                    }
                }
            }
        }
        System.out.println ("Fallito");
        if (Math.abs (start[0] - goal[0]) <= owner.getRange () && Math.abs (start[1] - goal[1]) <= owner.getRange ()) {
            System.out.println ("Raggiungibile");
        } else {
            System.out.println ("Irraggiungibile");
            System.out.println (Math.abs (start[0] - goal[0]));
            System.out.println (Math.abs (start[1] - goal[1]));
            System.out.println (Math.abs (start[0] - goal[0]) <= owner.getRange ());
            System.out.println (Math.abs (start[1] - goal[1]) <= owner.getRange ());
        }
        System.out.println ("Start" + start[0] + "-" + start[1] + " e Dest" + destx + " - " + desty + " con range " + owner.getRange ());
        return null;
    }

    public boolean isInBounds(int[] point) {
        return point[0] >= 0 && point[1] >= 0 && point[0] < owner.getWorld ().getFullWidth () && point[1] < owner.getWorld ().getFullHeight ();
    }

    public ArrayDeque<int[]> reconstructPath(HashMap cameFrom, int[] current) {
        double start = System.nanoTime ();
        ArrayDeque<int[]> path = new ArrayDeque<> ();
        path.add (current);
        while (cameFrom.containsKey (current)) {
            current = (int[]) cameFrom.get (current);
            path.addFirst (current);
        }
        return path;
    }

    public int[] wrapCoordinates(int[] point) {
        int[] wrap = new int[]{point[0], point[1]};
        if (point[0] < 0) {
            wrap[0] = owner.getWorld ().getFullWidth () + point[0];
        }
        if (point[1] < 0) {
            wrap[1] = owner.getWorld ().getFullHeight () + point[1];
        }
        if (point[0] >= owner.getWorld ().getFullWidth ()) {
            wrap[0] = point[0] - owner.getWorld ().getFullWidth ();
        }
        if (point[1] >= owner.getWorld ().getFullHeight ()) {
            wrap[1] = point[1] - owner.getWorld ().getFullHeight ();
        }
        return wrap;
    }

    /* NON UTILIZZATO MA ANDREBBE FIXATO PER IL WRAPPERONE
    public ArrayDeque<int[]> straightPath(int destx, int desty) {
        ArrayDeque<int[]> path = new ArrayDeque<> ();
        int cx = owner.getAbsx ();
        int cy = owner.getAbsy ();
        while (cx != destx || cy != desty) {
            int dx = destx - cx;
            int dy = desty - cy;
            if(dx>owner.getWorld ().getFullWidth () || dy>owner.getWorld ().getFullWidth ()){

            }else{

            }

            path.add (new int[]{cx, cy});
            if (cx < 0 || cy < 0 || cx > owner.getWorld ().getFullWidth () - 1 || cy > owner.getWorld ().getFullHeight () - 1) {
                break;
            }
        }
        for (int[] point : path) {
            action += " | " + point[0] + " - " + point[1];
        }
        return path;
    }

    OLD PATHING, TOO EXPENSIVE AND CRITTERS CAN'T KNOW EVERYTHING
    public ArrayDeque<int[]> choosePath() {
        weights = dijkPaths ();
        int range = owner.getRange ();
        World world = owner.getWorld ();
        int absx = owner.getAbsx ();
        int absy = owner.getAbsy ();
        int dietType = owner.getDietType ();

        int destx = absx, desty = absy;
        double max = 20; //MINIMUM WEIGHT
        double maxw = 0;
        double maxt = 0;
        double maxh = 0;
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                Cell c = world.getAbsCell (j + absx, i + absy);
                if (c != null) {
                    double hunger = 0, thirst = 0, weight = 0;
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
                        propension += (owner.getHunger () / 2) * (amount / 2);
                        hunger = (owner.getHunger () / 2) * (amount / 2);
                    }
                    if (c instanceof FreshWater) {
                        propension += owner.getThirst ();
                        thirst = owner.getThirst ();
                    }
                    propension -= weights[i + absy][j + absx] * 2;
                    propension *= propensionMap.get (c.getType ());
                    weight = weights[i + absy][j + absx] * 2;
                    if (propension > max) {
                        max = propension;
                        maxw = weight;
                        maxh = hunger;
                        maxt = thirst;
                        destx = j + absx;
                        desty = i + absy;
                    }
                }
            }
        }
        action += " | MAX " + max + " | WEIGHT " + maxw + " | FOOD " + maxh + " | THIRST " + maxt + " | ";
        if (destx == absx && desty == absy) { //WANDER
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
            action += "WANDER TO " + destx + "-" + desty;
        }
        return pathTo (destx, desty);
    }

    public ArrayDeque<int[]> pathTo(int destx, int desty) {
        int absx = owner.getAbsx ();
        int absy = owner.getAbsy ();

        //if (destx >= owner.getWorld ().getFullWidth () || desty >= owner.getWorld ().getFullHeight () || destx < 0 || desty < 0) {}  //DA RISCRIVERE MA NON HO SBATTI
        action += " | sPATH ";
        return straightPath (destx, desty);


        action += " | nPATH ";    UN GIORNO LO RIMETTO MA NON HO SBATTI VERAMENTE CIOÈ CHE COGLIONI STO WRAPPERONE
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
        for (int[] point : path) {
            action += " | "+point[0]+" - "+point[1];
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

        public ArrayDeque<int[]> straightPath(int destx, int desty) {
        ArrayDeque<int[]> path = new ArrayDeque<> ();
        int cx = owner.getAbsx ();
        int cy = owner.getAbsy ();
        while (cx != destx || cy != desty) {
            int dx = destx - cx;
            int dy = desty - cy;
            if (dx > 0) {
                cx++;
            }
            if (dy > 0) {
                cy++;
            }
            if (dx < 0) {
                cx--;
            }
            if (dy < 0) {
                cy--;
            }
            path.add (new int[]{cx, cy});
            if (cx < 0 || cy < 0 || cx > owner.getWorld ().getFullWidth () - 1 || cy > owner.getWorld ().getFullHeight () - 1) {
                break;
            }
        }
        for (int[] point : path) {
            action += " | " + point[0] + " - " + point[1];
        }
        return path;
    }
     */

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
                    if (owner.getDietType () >= 6 || owner.getDietType () <= 8) {
                        if (owner.getDietType () == 7) {
                            owner.setHunger (owner.getHunger () - interacting.getBiomass () / 2);
                        } else {
                            owner.setHunger (owner.getHunger () - interacting.getBiomass () / 4);
                        }
                    }
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
