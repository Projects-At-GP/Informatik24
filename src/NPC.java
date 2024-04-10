import animator.*;

public class NPC extends BaseEntity {
    Animation anim;

    public NPC(Renderer renderer) {
        super(renderer);
    }

    @Override
    protected void awake(){
        this.x = 16F;
        this.y = 17F;
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
        if (this.x < 24){
            this.x += 3 * state.deltaTime;
        } else this.x = 16;
    }
}
