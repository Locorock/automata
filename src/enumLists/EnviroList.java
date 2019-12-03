package enumLists;

public enum EnviroList {
    Desert (60, 110, 0, 20, 30),
    Canyon (75, 110, 20, 50, 30),
    Forest (30, 60, 40, 65, 40),
    Glacial (0, 15, 0, 15, 40),
    Jungle (50, 110, 50, 110, 30),
    Plains (30, 65, 20, 50, 60),
    Savanna (45, 75, 10, 55, 50),
    Steppe (30, 60, 0, 20, 200),
    Taiga (10, 30, 25, 70, 50),
    Tundra (0, 30, 0, 25, 40),
    Wetland (10, 50, 60, 110, 40),
    Snowland (0, 15, 25, 110, 40);


    private final int tempMin, tempMax, humMin, humMax, rarity;

    EnviroList(int tempMin, int tempMax, int humMin, int humMax, int rarity) {
        this.tempMin = tempMin;
        this.humMin = humMin;
        this.tempMax = tempMax;
        this.humMax = humMax;
        this.rarity = rarity;
    }

    public int getTempMin() {
        return this.tempMin;
    }

    public int getHumMin() {
        return this.humMin;
    }

    public int getTempMax() {
        return this.tempMax;
    }

    public int getHumMax() {
        return this.humMax;
    }

    public int getRarity() {
        return this.rarity;
    }
}