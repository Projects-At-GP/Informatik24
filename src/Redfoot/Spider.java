package Redfoot;

import animator.Animation;
import dialogue.Text;
import enemy.ai.EnemyAI;
import enemy.ai.IntelligenceEnum;
import greenfoot.Greenfoot;
import vector.Vector2;

public class Spider extends BaseEnemy {
    public Spider(Renderer renderer, EnemyAI enemyAI, Vector2 pos) {
        super(renderer, enemyAI);
        this.hp = 50;
        this.col = new collider();
        this.col.octagon(0.8, 0.3);
        this.hasCollider = true;
        this.pos = pos;
    }

    public Spider(Renderer renderer, Vector2 pos) {
        this(renderer, new EnemyAI(IntelligenceEnum.NOOB_INTELLIGENCE), pos);
    }

    @Override
    protected void awake() {
        this.text = new Text(this.renderer.game, null);
        this.anim = new Animation("images/testEnemySheet.png", this, 16, 4, 1);
        this.anim.setAnim(2);
        this.anim.update();
    }

    @Override
    protected void entityTick(Game.State state) {
        super.entityTick(state);
    }
}
