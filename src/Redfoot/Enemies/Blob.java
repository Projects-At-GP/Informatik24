package Redfoot.Enemies;

import Redfoot.BaseEnemy;
import Redfoot.BaseEntity;
import Redfoot.Renderer;
import Redfoot.collider;
import animator.Animation;
import enemy.ai.EnemyAI;
import enemy.ai.IntelligenceEnum;
import vector.Vector2;

public class Blob extends BaseEnemy {
    public Blob(Renderer renderer, EnemyAI enemyAI, Vector2 pos, String world) {
        super(renderer, enemyAI, world);
        this.hp = 80;
        this.col = new collider();
        this.col.octagon(0.8, 0.3);
        this.hasCollider = true;
        this.pos = pos;
    }

    public Blob(Renderer renderer, Vector2 pos, String world) {
        this(renderer, new EnemyAI(IntelligenceEnum.BLOB), pos, world);
    }

    @Override
    protected void awake() {
        super.awake();
        this.anim = new Animation("images/BlobSheet.png", this, 16, 4, 1);
        this.anim.setAnim(2);
        this.anim.update();
    }
}
