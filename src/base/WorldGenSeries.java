package base;

import java.util.ArrayList;

public class WorldGenSeries {
    private ArrayList<Integer> al;
    private int index = 1;

    public WorldGenSeries() {
        al = new ArrayList<Integer> ();
        al.add (1);
        al.add (1);
        al.add (1);
        al.add (1);
    }

    public void next() {
        al.clear ();
        index++;
        for (int i = 0; i < index * 4; i++) {
            if (i % (index) == 0) {
                al.add (1);
            } else {
                al.add (2);
            }
        }
    }

    public int get(int i) {
        return al.get (i);
    }

    public int getSize() {
        return al.size ();
    }
}
