package base;

import enumLists.CellList;

import java.util.HashMap;

public class GeneLibrary {
    private static final HashMap<String, int[]> index = new HashMap<> ();
    private static int size;

    public GeneLibrary() {
        int offset = 0;
        for (GeneIds id : GeneIds.values ()) {
            String name = id.name ();
            int size = id.size;
            int type = id.type;
            index.put (name, new int[]{offset, size, type});
            offset += size;
        }
        size = offset;
    }

    public static int[] searchIndex(String name) {
        return index.get (name);
    }

    public static int getSize() {
        return size;
    }

    public static HashMap<String, int[]> getIndex() {
        return index;
    }

    public enum GeneIds {
        AppearanceRecognition (16, 0),
        AppearanceCluster (16, 0),
        MateRate (8, 2),
        WaterEff (8, 2),
        FoodEff (8, 2),
        BaseSpeed (8, 2),
        Height (8, 2),
        DietType (7, 1),
        Aggressiveness (32, 1),
        WebbedFeet (8, 2),
        Size (32, 1),
        PropensionCluster (CellList.values ().length * 8, 3),
        CrossingCluster (CellList.values ().length * 8, 3);


        private final int size, type; //TYPE: 0 = bit, 1 = cardinal, 2 = decimal, 3 = decimal cluster

        GeneIds(int size, int type) {
            this.size = size;
            this.type = type;
        }

        public int getSize() {
            return size;
        }
    }


}
