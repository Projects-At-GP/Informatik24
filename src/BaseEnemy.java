import enemy.ai.BaseEnemyInterface;
import enemy.ai.EnemyAI;
import enemy.ai.IntelligenceEnum;
import vector.Vector2;

public class BaseEnemy extends BaseEntity implements BaseEnemyInterface {
    protected final EnemyAI enemyAI;

    public BaseEnemy(Renderer renderer, EnemyAI enemyAI) {
        super(renderer);
        this.enemyAI = enemyAI;
    }

    public BaseEnemy(Renderer renderer) {
        this(renderer, new EnemyAI(IntelligenceEnum.LINE_OF_SIGHT_LVL0, 10));
    }

    @Override
    protected void entityTick(Game.State state) {
        super.entityTick(state);
        this.enemyAI.setPlayerPosCache(this.renderer.player.pos);
        this.enemyAI.autoAggro();
        this.enemyAI.increaseAggressionWearinessIfApplicable();
        this.enemyAI.alertToSwarm(this, this.getWorld().getObjects(BaseEnemyInterface.class));
        this.enemyAI.chaseIfPossible();  // maybe move to priorityTick()
    }

    @Override
    public void getAlerted(Vector2 targetPos) {
        this.enemyAI.aggro(targetPos);
    }

    @Override
    public Vector2 getPos() {
        return this.pos;
    }

    @Override
    public EnemyAI getAI() {
        return this.enemyAI;
    }
}
