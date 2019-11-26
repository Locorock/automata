package base;

import java.util.ArrayList;

public class Foods {
    int size = 0;
    private ArrayList<Double> growthRates = new ArrayList<> ();
    private ArrayList<String> foodTypes = new ArrayList<> ();
    private ArrayList<Double> foodAmounts = new ArrayList<> ();
    private ArrayList<Double> maxAmounts = new ArrayList<> ();
    private Enviro enviro;

    public Foods(Enviro e) {
        this.enviro = e;
    }

    public void addFood(double growthRate, String foodType, double foodAmount, double maxAmount) {
        growthRates.add (growthRate);
        foodTypes.add (foodType);
        foodAmounts.add (foodAmount);
        maxAmounts.add (maxAmount);
        size++;
    }

    public void grow() {
        for (int i = 0; i < size; i++) {
            if (foodAmounts.get (i) < maxAmounts.get (i)) {
                double amount = foodAmounts.get (i) + enviro.getHumidity () * growthRates.get (i) / 100;
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

    public ArrayList<String> getFoodTypes() {
        return foodTypes;
    }

    public String getFoodType(int index) {
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
