package events;

import base.Cell;
import base.Enviro;
import base.Event;
import baseCells.Solid;
import cells.RockySoil;

public class Meteor extends Event {
    public Meteor(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    @Override
    public void apply(int str, Enviro enviro) {
        Cell[][] grid = enviro.getGrid ();
        enviro.setQuakeStr (enviro.getQuakeStr () + str);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                Cell cell = grid[j][i];
                try {
                    Solid solid = (Solid) cell;
                    grid[j][i] = new RockySoil ("RockySoil", enviro);
                } catch (ClassCastException e) {
                    e.printStackTrace ();
                }
            }
        }
    }

    @Override
    public void remove(int str, Enviro enviro) {

    }
}