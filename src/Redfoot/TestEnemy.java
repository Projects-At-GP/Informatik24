package Redfoot;

import animator.Animation;
import enemy.ai.EnemyAI;
import enemy.ai.IntelligenceEnum;
import vector.Vector2;

public class TestEnemy extends BaseEnemy {
    public TestEnemy(Renderer renderer, EnemyAI enemyAI) {
        super(renderer, enemyAI);
        this.hp = 100;
        this.col = new collider();
        this.col.octagon(0.8, 0.3);
        this.hasCollider = true;
    }

    public TestEnemy(Renderer renderer) {
        this(renderer, new EnemyAI(IntelligenceEnum.LINE_OF_SIGHT_LVL0, 16));
    }

    @Override
    protected void awake() {
        this.pos = new Vector2(this.renderer.player.pos.x - 6, this.renderer.player.pos.y - 5);
        this.anim = new Animation("images/testEnemySheet.png", this, 16, 4, 1);
        this.anim.setAnim(2);
        this.anim.update();
    }

    @Override
    protected void entityTick(Game.State state) {
        super.entityTick(state);
    }
}
