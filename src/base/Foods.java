package base;

import java.util.ArrayList;

public class Foods {
    private final ArrayList<Integer> foodTypes = new ArrayList<> ();
    private final ArrayList<Double> foodAmounts = new ArrayList<> ();
    private final ArrayList<Double> maxAmounts = new ArrayList<> ();
    private final Enviro enviro;
    public ArrayList<Double> growthRates = new ArrayList<> ();
    public boolean toDelete = false;

    public Foods(Enviro e) {
        this.enviro = e;
    }

    public void addFood(double growthRate, int foodType, double foodAmount, double maxAmount) {
        growthRates.add (growthRate / 4);
        foodTypes.add (foodType);
        foodAmounts.add (foodAmount);
        maxAmounts.add (maxAmount / 10); //test
    }

    public void tick() {
        for (int i = 0; i < foodTypes.size (); i++) {
            if (foodAmounts.get (i) < maxAmounts.get (i)) {
                double amount = foodAmounts.get (i) + enviro.getHumidity () * growthRates.get (i) / 30;
                this.foodAmounts.set (i, amount);
                if (foodAmounts.get (i) <= 0) {
                    foodAmounts.remove (i);
                    foodTypes.remove (i);
                    growthRates.remove (i);
                    maxAmounts.remove (i);
                    i--;
                    toDelete = foodAmounts.size () == 0;
                }
            }

        }
    }

    public void init() {
        for (int i = 0; i < foodTypes.size (); i++) {
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

    public double getFood(int index) {
        double amount = this.foodAmounts.get (index) / 20;
        return amount;
    }

    public ArrayList<Double> getMaxAmounts() {
        return maxAmounts;
    }

    public ArrayList<Double> getGrowthRates() {
        return growthRates;
    }

    public void setGrowthRates(ArrayList<Double> growthRates) {
        this.growthRates = growthRates;
    }

    public String toString(int i) {
        return "Type " + getFoodTypes ().get (i) + " | Amount " + getFoodAmount (i) + " | MaxAmount " + getMaxAmounts ().get (i) + " | GrowthRate " + getGrowthRates ().get (i);
    }

    public void removeFood(int index, double amount) {
        this.foodAmounts.set (index, this.foodAmounts.get (index) - amount);
    }

    public void addFoodToExisting(int index, double amount) {
        this.foodAmounts.set (index, this.foodAmounts.get (index) + amount);
    }
}
