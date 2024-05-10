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
        if (this.isDead || this.pos == null) return;
        Vector2 wanderInstruction;
        
        this.enemyAI.setPlayerPosCache(this.renderer.player.pos);
        this.enemyAI.aggro(this.renderer.player.pos);  //this.enemyAI.autoAggro();  // TODO: remove demo placeholder
        this.enemyAI.increaseAggressionWearinessIfApplicable(this);
        this.enemyAI.alertToSwarm(this, this.getWorld().getObjects(BaseEnemy.class));
        if (this.enemyAI.chaseIfPossible(this, state)) {  // maybe move to priorityTick()
            this.anim.update();
            this.anim.resume();
        } else if (this.pos.subtract(wanderInstruction = Algorithms.getWanderInstruction(this.renderer.exportToMapData(), this.pos)).magnitude() != 0) {
            this.pos.x += wanderInstruction.x * state.deltaTime * this.enemyAI.intelligence.speed/2;
            this.pos.y += wanderInstruction.y * state.deltaTime * this.enemyAI.intelligence.speed/2;

            if ((int) wanderInstruction.x > 0) {
                this.anim.setAnim(2);
            } else if ((int) wanderInstruction.x < 0) {
                this.anim.setAnim(1);
            } else if ((int) wanderInstruction.y > 0) {
                this.anim.setAnim(0);
            } else if ((int) wanderInstruction.y < 0) {
                this.anim.setAnim(3);
            }
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
