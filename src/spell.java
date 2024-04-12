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

    public spell(Renderer renderer, float x, float y){
        super(renderer);
        this.x = x;
        this.y = y;

        GreenfootImage img = new GreenfootImage(16, 16);
        img.setTransparency(0);
        setImage(img);
        this.col = new collider();
        this.col.octagon(0.3);
        this.hasCollider = true;
        this.isStatic = false;
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
        this.x += dx * speed * state.deltaTime;
        this.y += dy * speed * state.deltaTime;
    }

    public void rotate(){
        greenfoot.MouseInfo mouse = Greenfoot.getMouseInfo();

        int dmx = mouse.getX() - 800;
        int dmy = mouse.getY() - (450 - 32);

        double root = Math.sqrt((dmx * dmx) + (dmy * dmy));
        this.dx = (float) (dmx / root);
        this.dy = (float) (dmy / root);

        System.out.printf("Mouse x: %d, y: %d\n", mouse.getX(), mouse.getY());
    }

    @Override
    protected void onCollision(BaseActor other, Vector2 mtv){
        System.out.println("Spell hit!");
        if (other.getClass() == NPC.class) {
            ((NPC) other).takeDamage(15);
        }
        renderer.entities.remove(this);
        this.getWorld().removeObject(this);
    }
}
