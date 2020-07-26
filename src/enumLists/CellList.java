package enumLists;

import java.util.Arrays;
import java.util.List;

public enum CellList {
    Ambrosia (3, 16, true, Arrays.asList ("PrimSoup"), 1.2),
    BerryBush (3, 6, true, Arrays.asList ("Forest", "Plains", "Taiga"), 1.5),
    BirthingBoughs (3, 0.3, true, Arrays.asList ("PrimSoup", "Plains", "Forest", "Desert", "Wetlands", "Savanna", "Taiga"), 1),
    Cactus (3, 16, true, Arrays.asList ("Desert"), 1.2),
    Dirt (1, -1, true, Arrays.asList ("Forest", "Savanna", "Wetland", "Jungle", "Steppe"), 1),
    ForestTree (3, 6, true, Arrays.asList ("Forest", "Jungle", "Wetland"), 1.3),
    FruitTree (3, 6, true, Arrays.asList ("Forest", "Jungle", "Taiga"), 1.3),
    HighGrass (2, 16, true, Arrays.asList ("Plains", "Savanna", "Steppe"), 1.4),
    Lava (2, 1, false, Arrays.asList (""), 1),
    Ice (1, -1, false, Arrays.asList ("Glacial"), 1),
    Lichen (1, 1, true, Arrays.asList ("Tundra"), 1),
    LowGrass (1, 1, true, Arrays.asList ("Forest", "Plains", "Savanna", "Wetland", "Jungle", "Taiga", "Steppe", "PrimSoup"), 1.1),
    PlainsTree (3, 1, true, Arrays.asList ("Plains", "Savanna"), 1.3),
    Puddle (4, -1, false, Arrays.asList (""), 1.2),
    RiverWater (4, -1, true, Arrays.asList (""), 1.4),
    RockySoil (1, -1, false, Arrays.asList ("Tundra", "Glacial", "Canyon"), 1),
    SaltWater (1, 1, false, Arrays.asList ("Ocean"), 1.3),
    Sand (1, -1, true, Arrays.asList ("Desert"), 1.3),
    Shrub (3, 12, true, Arrays.asList ("Forest", "Plains", "Taiga", "Savanna"), 1.5),
    Snow (1, 1, true, Arrays.asList ("Taiga", "Snowland"), 1.3),
    SwampWater (2, 2, false, Arrays.asList ("Wetland"), 1.3),
    TaigaTree (3, 4, true, Arrays.asList ("Taiga", "Snowland"), 1.3),
    None (1, 0, false, Arrays.asList (""), 0);


    private final double humMult;
    private final List<String> biomes;
    private final int phase;
    private final boolean permeable;
    private final double occultation;

    CellList(int phase, double humMult, boolean permeable, List<String> biomes, double occultation) {
        this.humMult = humMult;
        this.biomes = biomes;
        this.phase = phase;
        this.permeable = permeable;
        this.occultation = occultation;
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

    public double getOccultation() {
        return occultation;
    }
}