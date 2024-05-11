package Redfoot;

import java.util.ArrayList;
import java.util.Iterator;

public class Weapon extends Item{
    protected int damage;
    public double cooldown;
    public final WeaponEnum values;

    public Weapon(Renderer renderer, WeaponEnum values) {
        super(renderer, values.imgPath);
        this.values = values;
        this.cooldown = values.cooldown;
        this.damage = values.damage;
    }

    public void doDamage(Player player, int direction){
        ArrayList<BaseEntity> takeDamageList = new ArrayList<>();
        for (BaseEntity actor : renderer.getEntities()) {
            if (actor.pos.subtract(player.pos).magnitude() <= 2){
                takeDamageList.add(actor);
            }
        }
        for (BaseEntity actor : takeDamageList){
            actor.takeDamage(damage);
        }
    }
}
