package Redfoot;

import animator.Animation;
import dialogue.Text;
import greenfoot.Greenfoot;
import vector.Vector2;

public class BaseEntity extends BaseActor{

    double hp;
    public boolean isDead = false;
    public boolean collided;
    protected Animation anim;
    public Text text;
    public UI health;

    public BaseEntity(Renderer renderer) {
        super(renderer);
        this.health = new UI(renderer);
    }

    public void takeDamage(double dmg){
        this.text.popup("\\$FF0000" + (int) dmg, new Vector2(this.pos.x - Greenfoot.getRandomNumber(2), this.pos.y - 1), 2000);
        hp -= dmg;
        if(hp <= 0) {
            renderer.ceaseEntity(this);
            this.getWorld().removeObject(this);
            logger.info("I am dead");
            this.isDead = true;
            renderer.game.spawnEntity();
            return;
        }
        logger.info(String.format("Took %d Damage! Now at %d HP", (int) dmg, (int) hp));
    }
}
