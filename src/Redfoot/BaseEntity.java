package Redfoot;

import animator.Animation;
import dialogue.Text;
import greenfoot.Greenfoot;
import vector.Vector2;

public class BaseEntity extends BaseActor{

    double hp;
    public boolean isDead = false;
    public boolean collided;
    public Animation anim;
    public Text text;
    public UI health;
    public boolean healthInWorld = false;
    public boolean interactable = false;

    public BaseEntity(Renderer renderer) {
        super(renderer);
        this.health = new UI(renderer);
    }

    public void takeDamage(double dmg){
        hp -= dmg;
        this.text.popup("\\$FF0000-" + (int) dmg, new Vector2(this.pos.x - Greenfoot.getRandomNumber(2), this.pos.y - 1), 2000);
        this.text.showText("\\$FFFFFF" + (int) this.hp, this.health);
        if(hp <= 0) {
            renderer.ceaseEntity(this);
            this.getWorld().removeObject(this.health);
            this.getWorld().removeObject(this);
            logger.info("I am dead");
            return;
        }
        logger.info(String.format("Took %d Damage! Now at %d HP", (int) dmg, (int) hp));
    }

    protected void interactable(){}

    @Override
    protected void awake() {
        super.awake();
        this.text = new Text(this.renderer.game, null);
    }
}
