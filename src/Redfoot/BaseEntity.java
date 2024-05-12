package Redfoot;

import animator.Animation;
import dialogue.Text;
import greenfoot.Greenfoot;
import vector.Vector2;

public class BaseEntity extends BaseActor{

    double hp;  // the health points
    public boolean isDead = false;  // indicator to signal whether any actions may be executed
    public boolean collided;  // whether the entity collided
    public Animation anim;  // the animation of the entity
    public Text text;  // the text field/support for the entity
    public UI health;
    public boolean healthInWorld = false;
    public boolean interactable = false;  // indicator to signal whether the entity may be interacted with
    public boolean active;  // indicator to signal whether the entity is active or not
    public String world;  // the dimension the entity belongs to

    public BaseEntity(Renderer renderer) {
        super(renderer);
        this.health = new UI(renderer);
        this.active = true;
    }

    /**
     * Take damage and display the amount it took
     * @param dmg the amount of damage to take
     */
    public void takeDamage(double dmg){
        if(!this.active) return;
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

    /**
     * Finish initialization whilst it being placed in the world
     */
    @Override
    protected void awake() {
        super.awake();
        this.text = new Text(this.renderer.game, null);
    }
}
