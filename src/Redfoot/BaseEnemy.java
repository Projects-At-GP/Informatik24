package Redfoot;

import enemy.ai.Algorithms;
import enemy.ai.EnemyAI;
import enemy.ai.IntelligenceEnum;
import vector.Vector2;

import java.util.LinkedList;

public class BaseEnemy extends BaseEntity {
    public final EnemyAI enemyAI;
    public LinkedList<Vector2> path;

    public BaseEnemy(Renderer renderer, EnemyAI enemyAI) {
        super(renderer);
        this.enemyAI = enemyAI;
        this.path = new LinkedList<>();
    }

    public BaseEnemy(Renderer renderer) {
        this(renderer, new EnemyAI(IntelligenceEnum.PRO_INTELLIGENCE));
    }

    @Override
    protected void entityTick(Game.State state) {
        super.entityTick(state);
        if (this.isDead) return;
        this.enemyAI.setPlayerPosCache(this.renderer.player.pos);
        this.enemyAI.aggro(this.renderer.player.pos);  //this.enemyAI.autoAggro();  // TODO: remove demo placeholder
        this.enemyAI.increaseAggressionWearinessIfApplicable(this);
        this.enemyAI.alertToSwarm(this, this.getWorld().getObjects(BaseEnemy.class));
        if (this.enemyAI.chaseIfPossible(this, state)) {  // maybe move to priorityTick()
            this.anim.update();
            this.anim.resume();
        }
    }

    @Override
    protected void pathfindingTick(Game.State state) {
        this.path = this.enemyAI.retrievePath(this, state);
    }

    public void getAlerted(Vector2 targetPos) {
        this.enemyAI.aggro(targetPos);
    }
}
