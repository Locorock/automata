package base;

import java.util.ArrayList;

public class Foods {
    int size = 0;
    private ArrayList<Double> growthRates = new ArrayList<> ();
    private ArrayList<Integer> foodTypes = new ArrayList<> ();
    private ArrayList<Double> foodAmounts = new ArrayList<> ();
    private ArrayList<Double> maxAmounts = new ArrayList<> ();
    private Enviro enviro;

    public Foods(Enviro e) {
        this.enviro = e;
    }

    public void addFood(double growthRate, int foodType, double foodAmount, double maxAmount) {
        growthRates.add (growthRate);
        foodTypes.add (foodType);
        foodAmounts.add (foodAmount);
        maxAmounts.add (maxAmount / 5); //test
        size++;
    }

    public void grow() {
        for (int i = 0; i < size; i++) {
            if (foodAmounts.get (i) < maxAmounts.get (i)) {
                double amount = foodAmounts.get (i) + enviro.getHumidity () * growthRates.get (i) / 30;
                this.foodAmounts.set (i, amount);
            }

        }
    }

    public void init() {
        for (int i = 0; i < size; i++) {
            double amount = maxAmounts.get (i);
            this.foodAmounts.set (i, amount);
        }
    }

    public ArrayList<Integer> getFoodTypes() {
        return foodTypes;
    }

    public int getFoodType(int index) {
        return foodTypes.get (index);
    }

    public double getFoodAmount(int index) {
        return foodAmounts.get (index);
    }

    public void setFoodAmounts(int index, double amount) {
        this.foodAmounts.set (index, amount);
    }

    public double eatFood(int index) {
        double amount = this.foodAmounts.get (index) / 20;
        this.foodAmounts.set (index, this.foodAmounts.get (index) - amount);
        return amount;
    }
}
