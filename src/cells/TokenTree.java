package cells;

import base.Appearance;
import base.Enviro;
import baseCells.Tree;

public class TokenTree extends Tree {
    static final double speedMod = 0.9;
    static final Appearance code = new Appearance ("", "", "", "", "");
    public TokenTree(String type, Enviro enviro) {
        super (type, enviro, code, speedMod, 2, true);
    }
}
