package enumLists;

import java.util.Arrays;
import java.util.List;

public enum CellList {
    BerryBush (3, 4, true, Arrays.asList ("Forest", "Plains", "Taiga")),
    Shrub (3, 12, true, Arrays.asList ("Forest", "Plains", "Taiga", "Savanna")),
    Cactus (3, 32, true, Arrays.asList ("Desert")),
    None (1, 0, false, Arrays.asList ("")),
    Dirt (1, -1, true, Arrays.asList ("Forest", "Savanna", "Wetland", "Jungle", "Steppe")),
    FruitTree (3, 4, true, Arrays.asList ("Forest", "Jungle", "Taiga")),
    LowGrass (1, 1, true, Arrays.asList ("Forest", "Plains", "Savanna", "Wetland", "Jungle", "Taiga", "Steppe")),
    HighGrass (2, 32, true, Arrays.asList ("Plains", "Savanna", "Steppe")),
    Ice (1, -1, false, Arrays.asList ("Glacial")),
    Lichen (1, 1, true, Arrays.asList ("Tundra")),
    SwampWater (2, 2, false, Arrays.asList ("Wetland")),
    RockySoil (1, -1, false, Arrays.asList ("Tundra", "Glacial")),
    Sand (1, -1, true, Arrays.asList ("Desert")),
    Snow (1, 1, true, Arrays.asList ("Taiga")),
    ForestTree (3, 6, true, Arrays.asList ("Forest", "Jungle", "Wetland")),
    PlainsTree (3, 1, true, Arrays.asList ("Plains", "Savanna")),
    TaigaTree (3, 4, true, Arrays.asList ("Taiga")),
    Carcass (4, 1, true, Arrays.asList ("")),
    SaltWater (1, 1, false, Arrays.asList ("Ocean")),
    Puddle (4, -1, false, Arrays.asList ("")),
    RiverWater (4, -1, true, Arrays.asList (""));


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