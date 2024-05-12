package Redfoot;

import animator.Animation;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class spell extends BaseEntity {

    Animation anim;
    private int speed = 10;
    private int damage = 25;

    private boolean awoke = false;

    private float dx;
    private float dy;
    private Vector2 dv;
    private boolean hit = false;
    private int deathAnimCounter;

    public spell(Renderer renderer, Vector2 pos, String world) {
        super(renderer, world);
        this.pos = pos;

        GreenfootImage img = new GreenfootImage(16, 16);
        img.setTransparency(0);
        setImage(img);
        this.col = new collider();
        this.col.octagon(0.3);
        this.hasCollider = true;
    }

    @Override
    protected void awake() {
        super.awake();
        this.anim = new Animation("images/fireballSheet.png", this, 8, 4, 1);
        this.anim.setAnim(0);
        this.awoke = true;
    }

    @Override
    protected void entityTick(Game.State state) {
        if (!awoke) return;
        if (!hit) this.anim.update();
        this.anim.resume();
    }

    @Override
    protected void priorityTick(Game.State state) {
        if (this.hit) {
            this.anim.update();
            if (this.deathAnimCounter <= 0) {
                renderer.ceaseEntity(this);
                this.getWorld().removeObject(this);
            }
            this.deathAnimCounter--;
            return;
        }
        Vector2 dvscaled = dv.scale(speed * state.deltaTime);
        this.pos = this.pos.add(dvscaled);
    }

    @Override
    protected void blockTick(Game.State state) {
        if (renderer.player.pos.subtract(this.pos).magnitude() > 50) { // 50 blocks range
            renderer.ceaseEntity(this);
            this.getWorld().removeObject(this);
        }
    }

    public void rotate() {
        greenfoot.MouseInfo mouse = Greenfoot.getMouseInfo();
        Vector2 dmouse = new Vector2(mouse.getX() - 800, mouse.getY() - (450 - 32));
        this.dv = dmouse.normalize();
    }

    @Override
    protected void onCollision(BaseActor other, Vector2 mtv) {
        if (other.getClass() == spell.class || other.getClass() == Player.class) return;
        if (other instanceof BaseEntity) {
            System.out.println("Spell hit!");
            ((BaseEntity) other).takeDamage(damage);
        }
        List<BaseEntity> entitiesToDoDamage = new ArrayList<>();
        for (BaseEntity actor : renderer.getEntities()) {
            if (actor == null || actor.getClass() == spell.class || actor.getClass() == Player.class) continue;
            if (actor.pos == null) continue;
            if (actor.pos.subtract(renderer.player.pos).magnitude() <= 3) {
                entitiesToDoDamage.add(actor);
            }
        }
        for (BaseEntity entity : entitiesToDoDamage) {
            entity.takeDamage(damage);
        }

        this.hit = true;
        this.col = null;
        this.hasCollider = false;
        this.anim = new Animation("images/fireballHitSheet.png", this, 32, 4, 1);
        this.deathAnimCounter = this.anim.frameCount;
    }
}
