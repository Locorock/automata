package enumLists;

import java.util.Arrays;
import java.util.List;

public enum CellList {
    BerryBush (3, 4, true, Arrays.asList ("Forest", "Plains", "Taiga")),
    Bush (3, 8, true, Arrays.asList ("Forest", "Plains", "Taiga", "Savanna")),
    Cactus (3, 32, true, Arrays.asList ("Desert")),
    Carcass (4, 0, true, Arrays.asList ("")),
    Dirt (1, -1, true, Arrays.asList ("Forest", "Plains", "Savanna", "Wetland", "Jungle", "Steppe")),
    FruitTree (3, 4, true, Arrays.asList ("Forest", "Jungle", "Taiga", "Wetland")),
    Grass (1, 1, true, Arrays.asList ("Forest", "Plains", "Savanna", "Wetland", "Jungle", "Taiga", "Steppe")),
    HighGrass (2, 2, true, Arrays.asList ("Plains", "Savanna", "Steppe")),
    FreshWater (4, 0, false, Arrays.asList ("")),
    Ice (1, -1, false, Arrays.asList ("Glacial")),
    Lichen (1, 1, true, Arrays.asList ("Tundra")),
    LowWater (2, 2, false, Arrays.asList ("Swamp")),
    None (1, 0, false, Arrays.asList ("")),
    RockySoil (1, -1, false, Arrays.asList ("Tundra", "Glacial")),
    Sand (1, -1, true, Arrays.asList ("Desert")),
    Snow (1, 1, true, Arrays.asList ("Taiga")),
    Tree (3, 8, true, Arrays.asList ("Forest", "Taiga", "Jungle", "Wetland")),
    Puddle (1, 0, false, Arrays.asList (""));

    private final double humMult;
    private final List<String> biomes;
    private final int phase;
    private final boolean permeable;

    CellList(int phase, double humMult, boolean permeable, List<String> biomes) {
        this.humMult = humMult;
        this.biomes = biomes;
        this.phase = phase;
        this.permeable = permeable;
    }

    public double getHumMult() {
        return humMult;
    }

    public List<String> getBiomes() {
        return biomes;
    }

    public int getPhase() {
        return phase;
    }

    public boolean isPermeable() {
        return permeable;
    }
}