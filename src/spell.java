import animator.Animation;
import greenfoot.*;
import vector.Vector2;

import java.awt.*;

public class spell extends BaseEntity {

    Animation anim;
    private int speed = 10;

    private boolean awoke = false;

    private float dx;
    private float dy;
    private Vector2 dv;

    public spell(Renderer renderer, Vector2 pos){
        super(renderer);
        this.pos = pos;

        GreenfootImage img = new GreenfootImage(16, 16);
        img.setTransparency(0);
        setImage(img);
        this.col = new collider();
        this.col.octagon(0.3);
        this.hasCollider = true;
    }

    @Override
    protected void awake(){
        this.anim = new Animation("images/fireballSheet.png", this, 8, 4, 1);
        this.anim.setAnim(0);
        this.awoke = true;
    }

    @Override
    protected void entityTick(Game.State state) {
        if(!awoke) return;
        this.anim.update();
        this.anim.resume();
    }

    @Override
    protected void priorityTick(Game.State state) {
        Vector2 dvscaled = dv.scale(speed * state.deltaTime);
        this.pos = this.pos.add(dvscaled);
        if(renderer.player.pos.subtract(this.pos).magnitude() > 50){ // 50 blocks range
            renderer.entities.remove(this);
            this.getWorld().removeObject(this);
        }
    }

    public void rotate(){
        greenfoot.MouseInfo mouse = Greenfoot.getMouseInfo();
        Vector2 dmouse = new Vector2(mouse.getX() - 800, mouse.getY() - (450 - 32));
        this.dv = dmouse.normalize();
    }

    @Override
    protected void onCollision(BaseActor other, Vector2 mtv){
        if(other.getClass() == spell.class || other.getClass() == Player.class) return;
        if (other.getClass() == NPC.class) {
            System.out.println("Spell hit!");
            ((NPC) other).takeDamage(15);
        }
        renderer.entities.remove(this);
        this.getWorld().removeObject(this);
    }
}
