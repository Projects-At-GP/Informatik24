public class BaseEntity extends BaseActor{

    double hp;
    public boolean collided;
    public BaseEntity(Renderer renderer) {
        super(renderer);
    }

    public void takeDamage(double dmg){
        hp -= dmg;
        if(hp <= 0) {
            renderer.entities.remove(this);
            this.getWorld().removeObject(this);
            logger.info("I am dead");
            return;
        }
        logger.info(String.format("Took %d Damage! Now at %d HP", (int) dmg, (int) hp));
    }
}
