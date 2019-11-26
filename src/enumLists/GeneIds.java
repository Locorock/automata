package enumLists;

public enum GeneIds {
    AppearanceRecognition (16),
    AppearanceCluster (16),
    AppearanceTolerance (16),
    MateRate (8),
    WaterEff (8),
    FoodEff (8),
    BaseSpeed (8),
    Height (8),
    DietType (4);


    private final int offset, size;
    int off = 0;

    GeneIds(int size) {
        this.offset = off;
        this.size = size;
        off += size;
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }
}
