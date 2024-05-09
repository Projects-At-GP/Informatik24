package Redfoot;

import animator.Animation;

public class BaseEntity extends BaseActor{

    double hp;
    public boolean isDead = false;
    public boolean collided;
    protected Animation anim;

    public BaseEntity(Renderer renderer) {
        super(renderer);
    }

    public void takeDamage(double dmg){
        hp -= dmg;
        if(hp <= 0) {
            renderer.entities.remove(this);
            this.getWorld().removeObject(this);
            logger.info("I am dead");
            this.isDead = true;
            renderer.game.spawnEntity();
            return;
        }
        logger.info(String.format("Took %d Damage! Now at %d HP", (int) dmg, (int) hp));
    }
}
