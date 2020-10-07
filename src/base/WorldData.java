package base;

import java.util.HashMap;

public class WorldData {
    HashMap<String, Double> critterGeneticBounds;
    HashMap<String, Double> critterParams;
    HashMap<String, Double> worldGenParams;
    HashMap<String, Double> enviroParams;

    public WorldData() { //SOON GENETIC
        critterGeneticBounds = new HashMap<> ();
        critterParams = new HashMap<> ();
        worldGenParams = new HashMap<> ();
        enviroParams = new HashMap<> ();

        critterGeneticBounds.put ("mateRateLower", shiftToRange (0.1, 1, 128, 256));
        critterGeneticBounds.put ("mateRateUpper", shiftToRange (1, 3, 128, 256));
        critterGeneticBounds.put ("speedLower", shiftToRange (0.1, 1, 128, 256));
        critterGeneticBounds.put ("speedUpper", shiftToRange (1, 3, 128, 256));
        critterGeneticBounds.put ("webbedLower", shiftToRange (0, 1.5, 128, 256));
        critterGeneticBounds.put ("webbedUpper", shiftToRange (1.5, 5, 128, 256));
        critterGeneticBounds.put ("aggressionLower", shiftToRange (0, 3, 128, 256));
        critterGeneticBounds.put ("aggressionUpper", shiftToRange (3, 15, 128, 256));
        critterGeneticBounds.put ("crossLower", shiftToRange (0, 1, 128, 256)); //
        critterGeneticBounds.put ("crossUpper", shiftToRange (1, 6, 128, 256)); //
        critterGeneticBounds.put ("propensionLower", shiftToRange (0, 1, 128, 256)); //
        critterGeneticBounds.put ("propensionUpper", shiftToRange (1, 6, 128, 256)); //

        critterParams.put ("longevitySizeMultiplier", shiftToRange (20, 80, 128, 256));
        critterParams.put ("longevityBaseValue", shiftToRange (100, 500, 128, 256));
        critterParams.put ("longevityMateMultiplier", shiftToRange (50, 100, 128, 256));
        critterParams.put ("mateCostHungerDenom", shiftToRange (4, 10, 128, 256));
        critterParams.put ("mateCostThirstDenom", shiftToRange (4, 10, 128, 256));
        critterParams.put ("mutationRate", shiftToRange (400, 1000, 0, 256));

        worldGenParams.put ("seaLevel", shiftToRange (0, 120, 128, 256));
        worldGenParams.put ("humMult", shiftToRange (0.5, 1.5, 128, 256));
        worldGenParams.put ("tempOffset", shiftToRange (-20, 20, 128, 256));
        worldGenParams.put ("landSize", shiftToRange (0.07, 0.13, 128, 256));

        enviroParams.put ("globalGrowthMultiplier", shiftToRange (0.8, 1.2, 128, 256)); //
    }

    public static double shiftToRange(double rangeStart, double rangeEnd, int value, int subdivisions) {
        return (((rangeEnd - rangeStart) * value) / subdivisions) + rangeStart;
    }
}

