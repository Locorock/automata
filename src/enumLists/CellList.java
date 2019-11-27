package enumLists;

import java.util.Arrays;
import java.util.List;

public enum CellList {
    Ambrosia (2, 16, true, Arrays.asList ("PrimSoup")),
    BerryBush (3, 4, true, Arrays.asList ("Forest", "Plains", "Taiga")),
    BirthingBoughs (3, 0.5, true, Arrays.asList ("PrimSoup")),
    Cactus (3, 16, true, Arrays.asList ("Desert")),
    Carcass (4, 1, true, Arrays.asList ("")),
    Dirt (1, -1, true, Arrays.asList ("Forest", "Savanna", "Wetland", "Jungle", "Steppe")),
    ForestTree (3, 6, true, Arrays.asList ("Forest", "Jungle", "Wetland")),
    FruitTree (3, 4, true, Arrays.asList ("Forest", "Jungle", "Taiga")),
    HighGrass (2, 32, true, Arrays.asList ("Plains", "Savanna", "Steppe")),
    Lava (2, 1, false, Arrays.asList ("")),
    Ice (1, -1, false, Arrays.asList ("Glacial")),
    Lichen (1, 1, true, Arrays.asList ("Tundra")),
    LowGrass (1, 1, true, Arrays.asList ("Forest", "Plains", "Savanna", "Wetland", "Jungle", "Taiga", "Steppe", "PrimSoup")),
    PlainsTree (3, 1, true, Arrays.asList ("Plains", "Savanna")),
    Puddle (4, -1, false, Arrays.asList ("")),
    RiverWater (4, -1, true, Arrays.asList ("")),
    RockySoil (1, -1, false, Arrays.asList ("Tundra", "Glacial")),
    SaltWater (1, 1, false, Arrays.asList ("Ocean")),
    Sand (1, -1, true, Arrays.asList ("Desert")),
    Shrub (3, 12, true, Arrays.asList ("Forest", "Plains", "Taiga", "Savanna")),
    Snow (1, 1, true, Arrays.asList ("Taiga")),
    SwampWater (2, 2, false, Arrays.asList ("Wetland")),
    TaigaTree (3, 4, true, Arrays.asList ("Taiga")),
    None (1, 0, false, Arrays.asList (""));


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