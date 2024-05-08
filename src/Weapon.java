public class Weapon extends Item{
    private final int damage = 20;

    public Weapon(Renderer renderer, String imgPath) {
        super(renderer, imgPath);
    }

    public void doDamage(Player player, int direction){
        for (BaseEntity actor : renderer.entities){
            if (actor.pos.subtract(player.pos).magnitude() <= 2){
                actor.takeDamage(damage);
                break;
            }
        }
    }
}
