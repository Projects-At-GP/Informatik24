package Redfoot;

import animator.Animation;
import vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class NPC3 extends BaseNPC {

    private List<Vector2> waypoints = new ArrayList<>();
    private int counterPath;
    private boolean hasTalked = false;

    private void populateSentences() { // needs linebreak every 28 chars. (\\n)
        this.sentences.add("MOIN! DU BIST NEU HIER ODER?\\nICH EMPFEHLE DIR, ZUM STADT-\\nAELTSTEN ZU GEHEN.\\nFOLGE MIR.");
    }

    public NPC3(Renderer renderer, String world) {
        super(renderer, world);
        populateSentences();
        this.pos = new Vector2(18, 19);
        this.anim = new Animation("images/NPC3Sheet.png", this, 16, 4, 1);
        this.waypoints.add(new Vector2(35, 19));
        this.waypoints.add(new Vector2(35, 25));
        this.waypoints.add(new Vector2(48, 31));
        this.waypoints.add(new Vector2(60, 31));
        this.goTo(this.pos);
    }

    @Override
    protected void interactWith() {
        if (System.currentTimeMillis() >= this.resetTimer) counter = 0;
        if (this.counter < this.sentences.size()) {
            this.renderer.showText(this.sentences.get(counter), this);
            this.hasTalked = true;
            this.resetTimer = System.currentTimeMillis() + timeToReset;
            counter++;
        } else this.renderer.showText("ICH HABE NICHTS MIT DIR ZU\\nBESPRECHEN.", this);
    }

    @Override
    protected void entityTick(Game.State state){
        super.entityTick(state);
        if(counterPath == this.waypoints.size()) counterPath = 0;
        if(!this.hasTalked) return;
        if (!this.hasArrived()) return;
        this.goTo(waypoints.get(counterPath));
        counterPath++;
    }
}
