package cells;

import base.Cell;
import base.Enviro;
import baseCells.LowWater;
import critters.Critter;
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
        Random r = enviro.getR ();
        if ((r.nextInt () * enviro.getTemperature ()) % 1000 == 0 || CellList.valueOf (previous.getType ()).isPermeable () && r.nextInt () % 4 == 0) {
            enviro.replaceWith (this, previous);
            previous.setUpdate (true);
        }
    }

    @Override
    public double getDrinkAmount() {
        return 50;
    }

    @Override
    public void onPassage(Critter critter) {
        super.onPassage (critter);
    }
}
