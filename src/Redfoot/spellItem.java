package Redfoot;

import vector.Vector2;

public class spellItem extends Weapon{
    public spellItem(Renderer renderer, String imgPath) {
        super(renderer, imgPath);
    }

    @Override
    public void doDamage(Player player, int direction){
        spell fire = new spell(renderer, player.pos);
        renderer.game.addObject(fire, 0, 0);
        renderer.entities.add(fire);
        fire.rotate();
    }
}
