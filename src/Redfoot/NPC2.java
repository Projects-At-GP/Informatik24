package Redfoot;

import animator.*;
import dialogue.Text;
import vector.Vector2;

import java.util.LinkedList;
import java.util.Queue;

public class NPC2 extends BaseNPC {
    private void populateSentences(){ // needs linebreak every 28 chars. (\\n)
        this.sentences.add("GRUESSE! ICH BIN HANS, DER  \\nKUERBISANBAUER. BRAUCHST DU \\nEINEN? ICH GENIESSE DAS     \\nRUHIGE DORFLEBEN FERNAB DER \\nSTADT.");
    }

    public NPC2(Renderer renderer) {
        super(renderer);
        populateSentences();
        this.pos = new Vector2(44, 66);
        this.anim = new Animation("images/NPC2Sheet.png", this, 16, 4, 1);
    }
}
