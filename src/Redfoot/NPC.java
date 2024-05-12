package Redfoot;

import animator.Animation;
import vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class NPC extends BaseNPC {

    private int currentWaypoint;
    private List<Vector2> waypoints = new ArrayList<>();
    private void populateSentences() { // needs linebreak every 28 chars. (\\n)
        this.sentences.add("HEY, ICH BIN NUR EIN        \\nDORFBEWOHNER. MEIN TAG?     \\nZAEHLEN VON SCHAFEN         \\nUND BEWUNDERN DER WOLKEN.   \\nNICHTS BESONDERES HIER.");
    }

    public NPC(Renderer renderer, String world) {
        super(renderer, world);
        populateSentences();
        this.pos = new Vector2(39, 15);
        this.anim = new Animation("images/NPCSheet.png", this, 16, 4, 1);
        this.goTo(new Vector2(20, 10));

        waypoints.add(new Vector2(20, 10));
        waypoints.add(new Vector2(39, 15));
    }

    @Override
    protected void entityTick(Game.State state){
        super.entityTick(state);
        if (this.hasArrived()) return;
        if(counter >= this.waypoints.size()) counter = 0;
        this.goTo(waypoints.get(counter));
    }
}
