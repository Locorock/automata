package baseCells;

import critters.Critter;

import java.util.ArrayList;

public interface Food {
    void onEat(Critter critter, int index);

    void init();

    ArrayList<String> getFoodTypes();

    Double getFoodAmount(int index);
}
