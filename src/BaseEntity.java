public class BaseEntity extends BaseActor{

    double hp;
    public BaseEntity(Renderer renderer) {
        super(renderer);
    }

    public void takeDamage(double dmg){
        hp -= dmg;
        logger.info(String.format("Took %d Damage! Now at %d HP", (int) dmg, (int) hp));
        if(hp <= 0) {
            renderer.entities.remove(this);
            this.getWorld().removeObject(this);
        }
    }
}
