import animator.*;

public class NPC extends BaseActor{

    Animation anim;
    Renderer render;
    EntityVisual visual;

    public NPC(Renderer render){
        this.render = render;
    }

    @Override
    protected void start(){
        anim = new Animation("./src/images/skeletonSheet.png", visual, 16, 4, 1);
    }

    @Override
    protected void priorityTick(Game.State state) {
        anim.update();
    }
}
