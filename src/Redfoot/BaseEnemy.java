package Redfoot;

import enemy.ai.EnemyAI;
import enemy.ai.IntelligenceEnum;
import vector.Vector2;

import java.util.LinkedList;

public class BaseEnemy extends BaseEntity {
    public final EnemyAI enemyAI;
    public LinkedList<Vector2> path;
    public boolean isWandering = false;
    public Vector2 spawnedAt;
    protected boolean damageAlerted = false;
    protected boolean handledDamageAlert = true;

    public BaseEnemy(Renderer renderer, EnemyAI enemyAI) {
        super(renderer);
        this.enemyAI = enemyAI;
        this.path = new LinkedList<>();
    }

    public BaseEnemy(Renderer renderer) {
        this(renderer, new EnemyAI(IntelligenceEnum.ABSENT_IQ));
    }

    @Override
    protected void awake() {
        super.awake();
        this.spawnedAt = new Vector2(this.pos.x, this.pos.y);
    }

    @Override
    protected void entityTick(Game.State state) {
        if(!this.active) return;
        super.entityTick(state);
        if (this.isDead || this.pos == null) return;

        this.enemyAI.setPlayerPosCache(this.renderer.player.pos);
        if (!this.enemyAI.autoAggro(this) && !this.gotDamaged()) this.enemyAI.reevaluateAggression(this);
        if (this.gotDamaged()) this.enemyAI.isAggro = true;  // to override range based evaluation
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

    @Override
    protected void pathfindingTick(Game.State state) {
        if (!this.handledDamageAlert) this.handledDamageAlert = true;
        this.path = this.enemyAI.retrievePath(this, state);
        if (this.damageAlerted) {
            this.damageAlerted = false;
            this.handledDamageAlert = false;
        }
    }

    public void getAlerted(Vector2 targetPos) {
        this.enemyAI.aggro(targetPos);
    }

    @Override
    public void takeDamage(double dmg) {
        super.takeDamage(dmg);
        this.getAlerted(this.renderer.player.pos);
        this.damageAlerted = true;
        if(this.isDead) this.renderer.player.killedEnemy(this);
    }

    public boolean gotDamaged() {  // TODO for tomorrow: rename to `bypassesChaseRadius` with a 5sec cooldown
        return this.damageAlerted || !this.handledDamageAlert;
    }
}
