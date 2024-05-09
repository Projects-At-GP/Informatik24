package Redfoot;

import animator.Animation;
import enemy.ai.EnemyAI;
import enemy.ai.IntelligenceEnum;
import greenfoot.Greenfoot;
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
        this(renderer, new EnemyAI(IntelligenceEnum.PRO_INTELLIGENCE));
    }

    @Override
    protected void awake() {
        this.pos = new Vector2(this.renderer.player.pos.x - (Greenfoot.getRandomNumber(6) - 2.5), this.renderer.player.pos.y - (Greenfoot.getRandomNumber(6) - 2.5));
        //this.pos = new Vector2(this.renderer.player.pos.x-3, this.renderer.player.pos.y+2);
        this.anim = new Animation("images/testEnemySheet.png", this, 16, 4, 1);
        this.anim.setAnim(2);
        this.anim.update();
    }

    @Override
    protected void entityTick(Game.State state) {
        super.entityTick(state);
    }
}
