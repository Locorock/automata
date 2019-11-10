package enumLists;

public enum EnviroList {
    Desert (40, 100, 0, 20, 30),
    Forest (0, 35, 20, 70, 40),
    Glacial (-30, -10, 0, 20, 40),
    Jungle (35, 70, 30, 100, 30),
    Plains (0, 40, 10, 50, 60),
    Savanna (35, 50, 0, 30, 50),
    Steppe (10, 40, 0, 20, 200),
    Taiga (-10, 10, 20, 60, 50),
    Tundra (-10, 10, 0, 20, 40),
    Wetland (10, 40, 40, 100, 40);


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