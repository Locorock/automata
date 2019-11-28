package baseCells;

import critters.Critter;

import java.util.ArrayList;

public interface Food {
    double onEat(Critter critter, int index);

    void init();

    ArrayList<Integer> getFoodTypes();

    Double getFoodAmount(int index);
}
