package cells;

import base.Enviro;
import baseCells.Tree;

public class TokenTree extends Tree {
    static final double speedMod = 0.9;
    public TokenTree(String type, Enviro enviro) {
        super (type, enviro, speedMod, 2, true);
    }
}
