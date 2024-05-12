package Redfoot;

import enemy.ai.EnemyAI;
import enemy.ai.IntelligenceEnum;
import vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class BaseNPC extends BaseEnemy {
    List<String> sentences = new ArrayList<>();
    long resetTimer;
    final int timeToReset = 10000;
    int counter;

    public BaseNPC(Renderer renderer) {
        super(renderer, new EnemyAI(IntelligenceEnum.NPC));
        this.hp = 100;
        this.col = new collider();
        this.col.octagon(0.8, 0.3);
        this.hasCollider = true;
        this.isInteractable = true;
    }

    @Override
    protected void start() {
        this.anim.update();
    }

    /**
     * Try to go to the desired position set by .goTo()
     */
    @Override
    protected void entityTick(Game.State state) {
        if (!this.active) return;
        if (this.isDead || this.pos == null) return;
        boolean moved = this.enemyAI.chaseOrWanderIfPossible(this, state);
        if (moved && this.anim != null) {
            this.anim.update();
            this.anim.resume();
        }
    }

    /**
     * Let the NPC go to a given position, may not be instant as the NPC needs to wait for the next pathfindingTick to be ticked
     *
     * @param target the desired position
     */
    protected void goTo(Vector2 target) {
        this.enemyAI.aggro(target);
    }

    /**
     * Overridden here to never bypass the chase radius
     */
    @Override
    public boolean bypassesChaseRadius(long curTick) {
        return false;
    }

    @Override
    protected void interactWith() {
        if (System.currentTimeMillis() >= this.resetTimer) counter = 0;
        if (this.counter < this.sentences.size()) {
            this.renderer.showText(this.sentences.get(counter), this);
            this.resetTimer = System.currentTimeMillis() + timeToReset;
            counter++;
        } else this.renderer.showText("ICH HABE NICHTS MIT DIR ZU\\nBESPRECHEN.", this);
    }

    /**
     * Overridden here to prevent NPCs to take damage -> make them invincible
     */
    @Override
    public void takeDamage(double dmg) {
    }
}
