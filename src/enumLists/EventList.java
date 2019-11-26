package enumLists;

import java.util.Arrays;
import java.util.List;

public enum EventList {
    Earthquake (20000, 0, 2, 1, 1, 0, null),
    Meteor (1000000, 0, 3, 1, 1.5, 0, null),
    Precipitation (2500, 1, 3, 3, 1.5, 1.5, null),
    Storm (5000, 1, 4, 2, 1.5, 1, null),
    Volcano (50000, 0, 3, 2, 1.5, 1, Arrays.asList ("Volcano"));

    private final int humAsc, rarity, meanSize, meanDuration;
    private final double stdSize, stdDuration;
    private final List<String> biomes;

    EventList(int rarity, int humAsc, int meanSize, int meanDuration, double stdSize, double stdDuration, List<String> biomes) {
        this.rarity = rarity;
        this.humAsc = humAsc;
        this.meanSize = meanSize;
        this.meanDuration = meanDuration;
        this.stdSize = stdSize;
        this.stdDuration = stdDuration;
        this.biomes = biomes;
    }

    public int getRarity() {
        return this.rarity;
    }

    public int getHumAsc() {
        return this.humAsc;
    }

    public List<String> getBiomes() {
        return biomes;
    }

    public int getMeanSize() {
        return meanSize;
    }

    public int getMeanDuration() {
        return meanDuration;
    }

    public double getStdSize() {
        return stdSize;
    }

    public double getStdDuration() {
        return stdDuration;
    }
}