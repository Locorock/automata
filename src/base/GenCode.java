package base;

import java.util.BitSet;
import java.util.Random;

public class GenCode {
    BitSet code;

    public GenCode(GenCode a, GenCode b, Random r) {
        code = (BitSet) a.getCode ().clone ();
        for (int i = 0; i < code.length (); i++) {
            if (r.nextInt (2) == 0) {
                code.set (i, b.getCode ().get (i));
            }
        }
        for (int i = 0; i < code.size (); i++) {
            if (r.nextInt (1000) == 0) {
                code.flip (i);
            }
        }
    }

    public GenCode(Random r) {
        code = new BitSet ();
        for (int j = 0; j < GeneLibrary.getSize (); j++) {
            code.set (j, r.nextBoolean ());
        }
    }

    public BitSet getGene(String name) {
        int[] index = GeneLibrary.searchIndex (name);
        return code.get (index[0], index[0] + index[1]);
    }

    public int getCardinality(String name) {
        int[] index = GeneLibrary.searchIndex (name);
        return code.get (index[0], index[0] + index[1]).cardinality ();
    }

    public static long convert(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length (); ++i) {
            value += bits.get (i) ? (1L << i) : 0L;
        }
        return value;
    }

    public int getDecimal(String name) {
        int[] index = GeneLibrary.searchIndex (name);
        return (int) convert (code.get (index[0], index[0] + index[1]));

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
