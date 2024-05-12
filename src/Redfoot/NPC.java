package Redfoot;

import animator.Animation;
import vector.Vector2;

public class NPC extends BaseNPC {
    private void populateSentences() { // needs linebreak every 28 chars. (\\n)
        this.sentences.add("HEY, ICH BIN NUR EIN        \\nDORFBEWOHNER. MEIN TAG?     \\nZAEHLEN VON SCHAFEN         \\nUND BEWUNDERN DER WOLKEN.   \\nNICHTS BESONDERES HIER.");
    }

    public NPC(Renderer renderer) {
        super(renderer);
        populateSentences();
        this.pos = new Vector2(23, 25);
        this.anim = new Animation("images/NPCSheet.png", this, 16, 4, 1);
    }
}
