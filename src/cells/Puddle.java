package cells;

import base.Cell;
import base.Critter;
import base.Enviro;
import baseCells.LowWater;
import enumLists.CellList;

import java.util.Random;

public class Puddle extends LowWater {
    Cell previous;
    public Puddle(String type, Enviro enviro, Cell previous) {
        super (type, enviro);
        this.previous = previous;
    }

    @Override
    public void tick() {
        super.tick ();
        Random r = enviro.getR ();
        if ((r.nextInt () * (int) enviro.getTemperature ()) % 500 == 0 || CellList.valueOf (previous.getType ()).isPermeable () && r.nextInt () % 4 == 0) {
            enviro.replaceWith (this, previous);
        }
    }

    @Override
    public void onPassage(Critter critter) {
        super.onPassage (critter);
    }
}
