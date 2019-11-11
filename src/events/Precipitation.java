package events;

import base.Cell;
import base.Enviro;
import base.Event;
import cells.Puddle;
import enumLists.CellList;

public class Precipitation extends Event {
    public Precipitation(Enviro epicenter, String name) {
        super (epicenter, name);
    }

    @Override
    public void apply(int str, Enviro enviro) {
        enviro.setRainStr (enviro.getRainStr () + str);
        Cell[][] grid = enviro.getGrid ();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                Cell cell = grid[j][i];
                if (CellList.valueOf (cell.getType ()).getPhase () == 1) {
                    if (r.nextInt (80) < str) {
                        grid[j][i] = new Puddle ("Puddle", enviro, grid[j][i]);
                    }
                }
            }
        }
        enviro.setHumidity (enviro.getHumidity () + 4);
        enviro.setTemperature (enviro.getTemperature () + ((20 - enviro.getTemperature ()) / 5));
    }

    @Override
    public void remove(int str, Enviro enviro) {
        enviro.setRainStr (enviro.getRainStr () - str);
    }
}