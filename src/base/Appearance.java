package base;

import critters.GenCode;

import java.util.BitSet;

public class Appearance {
    BitSet bitCodeVisual = new BitSet (8);
    BitSet bitCodeAuditory = new BitSet (8);
    BitSet bitCodeOlfactory = new BitSet (8);
    BitSet bitCodeGustatory = new BitSet (8);
    BitSet bitCodeTactile = new BitSet (8);

    public Appearance(String bitCodeVisual, String bitCodeAuditory, String bitCodeOlfactory, String bitCodeGustatory, String bitCodeTactile) {
        for (int i = 0; i < 8; i++) {
            if (bitCodeVisual != null) {
                this.bitCodeVisual.set (i, Boolean.getBoolean ("" + bitCodeVisual.charAt (i)));
            }
            if (bitCodeAuditory != null) {
                this.bitCodeAuditory.set (i, Boolean.getBoolean ("" + bitCodeAuditory.charAt (i)));
            }
            if (bitCodeOlfactory != null) {
                this.bitCodeAuditory.set (i, Boolean.getBoolean ("" + bitCodeOlfactory.charAt (i)));
            }
            if (bitCodeGustatory != null) {
                this.bitCodeGustatory.set (i, Boolean.getBoolean ("" + bitCodeGustatory.charAt (i)));
            }
            if (bitCodeTactile != null) {
                this.bitCodeTactile.set (i, Boolean.getBoolean ("" + bitCodeTactile.charAt (i)));
            }
        }
    }

    public double getPropensity(GenCode code) {
        int visualP = code.getHammingDiff ("VisualRecognition", bitCodeVisual);
        int auditoryP = code.getHammingDiff ("AuditoryRecognition", bitCodeAuditory);
        int olfactoryP = code.getHammingDiff ("OlfactoryRecognition", bitCodeOlfactory);
        int gustatoryP = code.getHammingDiff ("GustatoryRecognition", bitCodeGustatory);
        int tactileP = code.getHammingDiff ("TactileRecognition", bitCodeTactile);
        double result = (Math.log (visualP + 0.01) + Math.log (auditoryP + 0.01) + Math.log (olfactoryP + 0.01) + Math.log (gustatoryP + 0.01) + Math.log (tactileP + 0.01));
        return result;
    }
}
