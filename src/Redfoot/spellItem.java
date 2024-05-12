package Redfoot;

public class spellItem extends Weapon {
    public spellItem(Renderer renderer, WeaponEnum values) {
        super(renderer, values);
    }

    @Override
    public void doDamage(Player player, int direction) {
        spell fire = new spell(renderer, player.pos);
        renderer.game.addObject(fire, 0, 0);
        renderer.addEntity(fire);
        fire.rotate();
    }
}
