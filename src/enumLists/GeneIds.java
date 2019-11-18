package enumLists;

public enum GeneIds {
    VisualRecognition (0, 8),
    AuditoryRecognition (1, 8),
    OlfactoryRecognition (2, 8),
    GustatoryRecognition (3, 8),
    TactileRecognition (4, 8),
    CritterRecognition (2, 16),
    AppearanceCluster (3, 16);

    private final int id, offset, size;
    int off = 0;

    GeneIds(int id, int size) {
        this.id = id;
        this.offset = off;
        this.size = size;
        off += size;
    }

    public int getOffset() {
        return offset;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
}
