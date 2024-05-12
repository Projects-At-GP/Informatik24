package Redfoot;

import animator.Animation;
import vector.Vector2;

public class NPC extends BaseNPC {
    private void populateSentences() { // needs linebreak every 28 chars. (\\n)
        this.sentences.add("HEY, ICH BIN NUR EIN        \\nDORFBEWOHNER. MEIN TAG?     \\nZAEHLEN VON SCHAFEN         \\nUND BEWUNDERN DER WOLKEN.   \\nNICHTS BESONDERES HIER.");
    }

    public NPC(Renderer renderer, String world) {
        super(renderer, world);
        populateSentences();
        this.pos = new Vector2(39, 15);
        this.anim = new Animation("images/NPCSheet.png", this, 16, 4, 1);
        this.goTo(new Vector2(20, 10));
    }
}
