package enumLists;

public enum GeneIds {

    AppearanceRecognition (16),
    AppearanceCluster (16);

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
