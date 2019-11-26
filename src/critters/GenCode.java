package critters;

import enumLists.GeneIds;

import java.util.BitSet;
import java.util.Random;

public class GenCode {
    public static int GENES_NUMBER = GeneIds.values ().length;
    public static int GENES_SIZE = GeneIds.values ()[GeneIds.values ().length - 1].getOffset () + GeneIds.values ()[GeneIds.values ().length - 1].getSize ();
    Random r;
    BitSet code;

    public GenCode(GenCode a, GenCode b, Random r) {
        code = (BitSet) a.getCode ().clone ();
        for (int i = 0; i < code.length (); i++) {
            if (r.nextInt (2) == 0) {
                code.set (i, b.getCode ().get (i));
            }
        }
        for (int i = 0; i < code.size (); i++) {
            if (r.nextInt (100) == 0) {
                code.flip (i);
            }
        }
    }

    public GenCode(Random r) {
        code = new BitSet ();
        for (int j = 0; j < GENES_SIZE; j++) {
            code.set (j, r.nextBoolean ());
        }
    }

    public BitSet getGene(String name) {
        GeneIds gi = GeneIds.valueOf (name);
        return code.get (gi.getOffset (), gi.getOffset () + gi.getSize ());
    }

    public int getCardinality(String name) {
        GeneIds gi = GeneIds.valueOf (name);
        return code.get (gi.getOffset (), gi.getOffset () + gi.getSize ()).cardinality ();
    }

    public int getHammingDiff(String name, BitSet b) {
        if (b == null || getGene (name) == null) {
            return 0;
        }
        BitSet diff = (BitSet) getGene (name).clone ();
        diff.xor (b);
        return diff.cardinality ();
    }

    public BitSet getCode() {
        return code;
    }

    public void setCode(BitSet code) {
        this.code = code;
    }
}
