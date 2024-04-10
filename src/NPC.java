import animator.*;

public class NPC extends BaseEnemy{
    Animation anim;
    Renderer render;
    EntityVisual visual;

    public NPC(Renderer renderer) {
        super(renderer);
    }

    @Override
    protected void awake(){
        System.out.println("npc awake");
        //visual = new EntityVisual(20.5F, 20.5F);
        visual = new EntityVisual(800, 100);
        anim = new Animation("./src/images/skeletonSheet.png", visual, 16, 4, 1);
        render.visuals.add(visual);
    }

    @Override
    protected void start(){

    }

    @Override
    protected void priorityTick(Game.State state) {
        anim.update();
    }
}
