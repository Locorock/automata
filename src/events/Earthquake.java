package events;

import base.Cell;
import base.Enviro;
import base.Event;
import baseCells.Living;

public class Earthquake extends Event {
    public Earthquake(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    @Override
    public void apply(int str, Enviro enviro) {
        Cell[][] grid = enviro.getGrid ();
        enviro.setQuakeStr (enviro.getQuakeStr () + str);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                Cell cell = grid[j][i];
                if (cell.getType () == "Living") {
                    if (r.nextInt (10) < str) {
                        ((Living) cell).setAlive (false);
                    }
                }
            }
        }
    }

    @Override
    public void remove(int str, Enviro enviro) {
        enviro.setQuakeStr (enviro.getQuakeStr () - str);
    }
}
