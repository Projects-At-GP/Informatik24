package Redfoot;

public class spellItem extends Weapon {
    public spellItem(Renderer renderer, WeaponEnum values, String world) {
        super(renderer, values, world);
    }

    @Override
    public void doDamage(Player player, int direction) {
        spell fire = new spell(renderer, player.pos, this.world);
        renderer.game.addObject(fire, 0, 0);
        renderer.addEntity(fire);
        fire.rotate();
    }
}
