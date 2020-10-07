package base;

import java.util.BitSet;
import java.util.Random;

public class GenCode {
    BitSet code;
    static BitSet genericCode;

    //INHERIT GENOME
    public GenCode(GenCode a, GenCode b, Random r) {
        code = (BitSet) a.getCode ().clone ();
        for (int i = 0; i < code.length (); i++) {
            if (r.nextInt (2) == 0) {
                code.set (i, b.getCode ().get (i));
            }
        }
    }

    //RANODM GENOME
    public GenCode(Random r) {
        if (genericCode == null) {
            genericCode = new BitSet ();
            for (int j = 0; j < GeneLibrary.getSize (); j++) {
                genericCode.set (j, r.nextBoolean ());
                if (j >= GeneLibrary.getIndex ().get ("Size")[0] && j < GeneLibrary.getIndex ().get ("Size")[0] + GeneLibrary.getIndex ().get ("Size")[1]) {
                    genericCode.set (j, false);
                }
            }
        }
        code = (BitSet) genericCode.clone ();
    }

    //DEFINED GENOME WITH INTEGERS DA CONTROLLARE
    public GenCode(int[] genes, Random r) {
        code = new BitSet ();
        int i = 0, offset = 0;
        for (GeneLibrary.GeneIds key : GeneLibrary.GeneIds.values ()) {
            if (genes[i] != -1) {
                int[] bits = toBits (genes[i], key.getSize ());
                for (int j = 0; j < key.getSize (); j++) {
                    if (bits[j] == 1) {
                        code.set (j + offset);
                    }
                }
            } else {
                for (int j = 0; j < key.getSize (); j++) {
                    if (r.nextBoolean ()) {
                        code.set (j + offset);
                    }
                }
            }
            i++;
            offset += key.getSize ();
        }
    }

    public static long convert(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length (); ++i) {
            value += bits.get (i) ? (1L << bits.length () - i) : 0L;
        }
        return value;
    }

    public BitSet getGene(String name) {
        int[] index = GeneLibrary.searchIndex (name);
        return code.get (index[0], index[0] + index[1]);
    }

    public int getCardinality(String name) {
        int[] index = GeneLibrary.searchIndex (name);
        return code.get (index[0], index[0] + index[1]).cardinality ();
    }

    public static BitSet toBitset(int n) {
        BitSet bs = new BitSet ();
        String bits = Integer.toBinaryString (n);
        int i = 0;
        for (char c : bits.toCharArray ()) {
            if (c == '1') {
                bs.set (i);
            }
            i++;
        }
        return bs;
    }

    public static int[] toBits(int n, int size) {
        String bits = Integer.toBinaryString (n);
        int[] b = new int[size];
        for (int i = 0; i < size - bits.length (); i++) {
            b[i] = 0;
        }
        int i = 0;
        for (char c : bits.toCharArray ()) {
            int index = size - bits.length ();
            if (c == '1') {
                b[index] = 1;
            } else {
                b[index] = 0;
            }
            i++;
        }
        return b;
    }

    public void mutate(Random r, double rate) {
        for (int i = 0; i < code.size (); i++) {
            if (r.nextInt (5000) == 0) {
                code.flip (i);
            }
        }
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

    public int getFullDiff(GenCode gc) {
        BitSet diff = (BitSet) code.clone ();
        diff.xor (gc.getCode ());
        return diff.cardinality ();
    }

    public BitSet getCode() {
        return code;
    }

    public void setCode(BitSet code) {
        this.code = code;
    }
}
