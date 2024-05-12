package Redfoot;

import enemy.ai.EnemyAI;
import enemy.ai.IntelligenceEnum;
import vector.Vector2;

import java.util.LinkedList;

public class BaseEnemy extends BaseEntity {
    public final EnemyAI enemyAI;  // a dedicated AI-instance for the enemy
    public LinkedList<Vector2> path;  // a cached path to trace
    public boolean isWandering = false;  // true if the enemy is wandering; false if the enemy is chasing the player
    public Vector2 spawnedAt;  // the original spawn of the enemy
    protected long bypassChaseRadiusUntilTick = 0;  // bypasses radii checks until the tick is reached
    protected int pendingSecondsForChasingBypass = 0;  // seconds to set the bypass duration to when next priorityTick is ticked

    public BaseEnemy(Renderer renderer, EnemyAI enemyAI) {
        super(renderer);
        this.enemyAI = enemyAI;
        this.path = new LinkedList<>();
    }

    public BaseEnemy(Renderer renderer) {
        this(renderer, new EnemyAI(IntelligenceEnum.ABSENT_IQ));
    }

    /**
     * Finish initialization whilst it being placed in the world
     */
    @Override
    protected void awake() {
        super.awake();
        this.spawnedAt = new Vector2(this.pos.x, this.pos.y);
    }

    /**
     * Set bypasses if they are pending to be set
     */
    @Override
    protected void priorityTick(Game.State state) {
        super.priorityTick(state);
        if (this.pendingSecondsForChasingBypass > 0) {
            this.bypassChaseRadiusUntilTick = state.tick + (long) state.game.tps * this.pendingSecondsForChasingBypass;
            this.pendingSecondsForChasingBypass = 0;
        }
    }

    /**
     * Try to damage the player, on failure try to chase the player or alternatively wander around the original spawn
     */
    @Override
    protected void entityTick(Game.State state) {
        if(!this.active) return;
        super.entityTick(state);
        if (this.isDead || this.pos == null) return;

        this.enemyAI.setPlayerPosCache(this.renderer.player.pos);
        if (!this.enemyAI.autoAggro(this) && !this.bypassesChaseRadius(state.tick)) this.enemyAI.reevaluateAggression(this);
        if (this.bypassesChaseRadius(state.tick)) this.enemyAI.isAggro = true;  // to override range based evaluation
        this.enemyAI.alertToSwarm(this, this.getWorld().getObjects(BaseEnemy.class));

        boolean moved;
        if (this.enemyAI.damageIfPossible(this, state)) {
            moved = false;
        } else moved = this.enemyAI.chaseOrWanderIfPossible(this, state);

        if (moved && this.anim != null) {
            this.anim.update();
            this.anim.resume();
        }
    }

    /**
     * Update the cached path
     */
    @Override
    protected void pathfindingTick(Game.State state) {
        this.path = this.enemyAI.retrievePath(this, state);
    }

    /**
     * Alert the enemy to a given position
     * @param targetPos
     */
    public void getAlerted(Vector2 targetPos) {
        this.pendingSecondsForChasingBypass = 2;
        this.enemyAI.aggro(targetPos);
    }

    /**
     * Take damage and chase down the player afterward
     * @param dmg the amount of damage to take
     */
    @Override
    public void takeDamage(double dmg) {
        super.takeDamage(dmg);
        this.pendingSecondsForChasingBypass = 5;
        this.getAlerted(this.renderer.player.pos);
        if (this.isDead) this.renderer.player.killedEnemy(this);
    }

    /**
     * Helper to figure out whether the enemy may bypass their own restrictions
     * @param curTick the current tick
     * @return true if the enemy may bypass their chase radius
     */
    public boolean bypassesChaseRadius(long curTick) {
        return curTick < this.bypassChaseRadiusUntilTick || this.pendingSecondsForChasingBypass > 0;
    }
}
