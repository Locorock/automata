package baseCells;

import critters.Critter;

import java.util.ArrayList;

public interface Food {
    ArrayList<String> foodTypes = new ArrayList<> ();
    ArrayList<Double> foodAmounts = new ArrayList<> ();

    void onEat(Critter critter, int index);

    String getFoodType(int index);

    Double getFoodAmount(int index);

    void setFoodAmount(int index, double amount);
}
