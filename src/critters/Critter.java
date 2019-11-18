package critters;

import base.Cell;
import base.Enviro;
import baseCells.Food;
import baseCells.FreshWater;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Critter {
    public ArrayDeque<Cell> path;
    private Enviro enviro;
    private GenCode code;
    private int posx;
    private int posy;
    private Cell cell;
    private ArrayList<WMap> wMaps = new ArrayList<> ();
    private int range = 5; //HARDCODED FOR NOW
    private double thirst = 0;
    private double hunger = 0;
    private double speed = 1;
    private double baseSpeed = 1;
    private double movementProgress = 0;

    public Critter(Enviro enviro, int posx, int posy) {
        this.enviro = enviro;
        this.posx = posx;
        this.posy = posy;
        this.cell = this.enviro.getGrid ()[posx][posy];
        this.code = new GenCode (enviro.getR ());
    }

    public void tick() {
        //AGING AND STATUS EFFECTS
        if (path.isEmpty ()) {
            //DECISIONAL CODE, HARDCODED FOR NOW, MAY NOT MOVE AND INSTEAD INTERACT
            if (cell instanceof Food && ((Food) cell).getFoodAmount (0) > 0 && this.hunger > 0) {
                eatOnCell ();
                return;
            }
            if (cell instanceof FreshWater && this.hunger > 0) {
                drinkOnCell ();
                return;
            }
            path = choosePath ();
        } else {
            //MOVEMENT COST
            speed = baseSpeed * cell.getSpeedMod ();
            movementProgress += speed;
            while (movementProgress > 1) {
                movementProgress--;
                moveTo (path.removeFirst ());
            }
        }
    }

    public ArrayDeque<Cell> choosePath() {
        WMap wMap = null;
        boolean foundWeights = false;
        for (WMap w : wMaps) {
            if (w.getE ().equals (enviro)) {
                wMap = w;
                foundWeights = true;
            }
        }
        if (!foundWeights) {
            wMap = new WMap (enviro, this);
            wMaps.add (wMap);
        }
        double[][] paths = dijkPaths (wMap);
        double max = 0;
        Cell dest = null;
        for (int i = 0; i < enviro.getWidth (); i++) {
            for (int j = 0; j < enviro.getWidth (); j++) {
                double propension = paths[j][i];
                if (enviro.getGrid ()[j][i] instanceof Food) {
                    propension += hunger;
                }
                if (enviro.getGrid ()[j][i] instanceof FreshWater) {
                    propension += thirst;
                }
                if (propension > max) {
                    propension = max;
                    dest = enviro.getGrid ()[j][i];
                }
            }
        }
        ArrayDeque<Cell> path = new ArrayDeque<Cell> ();
        double min = Double.MAX_VALUE;
        Cell next = null;
        while (!dest.equals (cell)) {
            path.addFirst (dest);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0) && Math.abs (i + j) == 1 && dest.getX () + j >= 0 && dest.getX () + j < enviro.getWidth () && dest.getY () + i >= 0 && dest.getY () + i < enviro.getWidth ()) {
                        if (paths[j + dest.getX ()][i + dest.getY ()] < min) {
                            min = paths[j + dest.getX ()][i + dest.getY ()];
                            next = enviro.getGrid ()[j + dest.getX ()][i + dest.getY ()];
                        }
                    }
                }
            }
            dest = next;
        }
        return path;
    }

    public void eatOnCell() {
        Food food = (Food) cell;
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
    }

    public void drinkOnCell() {
        this.thirst -= 3;
    }

    public void moveTo(Cell dest) {
        this.posx = dest.getX ();
        this.posy = dest.getY ();
        this.cell = this.enviro.getGrid ()[posx][posy];
    }

    public double[][] dijkPaths(WMap wmap) {
        double[][] paths = new double[enviro.getWidth ()][enviro.getWidth ()];
        for (int i = 0; i < paths.length; i++) {
            for (int j = 0; j < paths.length; j++) {
                paths[j][i] = 9999999;
            }
        }
        ArrayList<Cell> visited = new ArrayList<Cell> ();
        ArrayList<Cell> open = new ArrayList<Cell> ();
        open.add (cell);
        paths[posx][posy] = 0;
        while (!open.isEmpty ()) {
            Cell min = null;
            double minValue = 99999;
            for (int i = 0; i < open.size (); i++) {
                Cell next = open.get (i);
                if (paths[next.getX ()][next.getY ()] < minValue) {
                    min = next;
                    minValue = paths[next.getX ()][next.getY ()];
                }
            }
            int x = min.getX ();
            int y = min.getY ();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0) && Math.abs (i + j) == 1 && x + j >= 0 && x + j < enviro.getWidth () && y + i >= 0 && y + i < enviro.getWidth () && Math.abs (posx - x) + Math.abs (posy - y) <= range) {
                        Cell next = enviro.getGrid ()[j + x][i + y];
                        if (!visited.contains (next)) {
                            if (paths[j + x][i + y] == -1 || paths[x][y] + wmap.getWeight (j + x, i + y) < paths[j + x][i + y]) {
                                paths[j + x][i + y] = paths[x][y] + wmap.getWeight (j + x, i + y);
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

    public double distanceFromTo(int x, int y, int toX, int toY) {
        return Math.sqrt (Math.pow (x - toX, 2) + Math.pow (y - toY, 2));
    }

    public double calcWeight(Cell cell) {
        return cell.getCode ().getPropensity (code);
    }
}
