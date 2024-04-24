package Redfoot;

import animator.*;
import vector.Vector2;

public class NPC extends BaseEntity {
    public NPC(Renderer renderer) {
        super(renderer);
        this.hp = 100;
        this.col = new collider();
        this.col.octagon(0.8, 0.3);
        this.hasCollider = true;
    }

    @Override
    protected void awake(){
        this.pos = new Vector2(16, 17);
        this.anim = new Animation("images/skeletonSheet.png", this, 16, 4, 1);
        this.anim.setAnim(2);
    }

    @Override
    protected void start(){

    }

    @Override
    protected void entityTick(Game.State state) {

        this.anim.update();
        this.anim.resume();
        if (this.pos.x < 24){
            this.pos.x += 3 * state.deltaTime;
        } else this.pos.x = 16;
    }
}
