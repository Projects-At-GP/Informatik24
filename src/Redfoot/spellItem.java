package Redfoot;

public class spellItem extends Weapon {
    public spellItem(Renderer renderer, WeaponEnum values, String world) {
        super(renderer, values, world);
    }

    /**
     * Attempt to deal damage via the fire spell itself
     * @param player the player instance
     * @param direction <i>unused</i>
     */
    @Override
    public void doDamage(Player player, int direction) {
        spell fire = new spell(renderer, player.pos, this.world);
        renderer.game.addObject(fire, 0, 0);
        renderer.addEntity(fire);
        fire.rotate();
    }
}
