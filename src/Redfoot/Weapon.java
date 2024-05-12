package Redfoot;

import java.util.ArrayList;

public class Weapon extends Item {
    protected int damage;  // the amount of damage to deal
    public double cooldown;  // cooldown to prevent attack spam
    public final WeaponEnum weaponType;  // the type of the weapon

    public Weapon(Renderer renderer, WeaponEnum weaponType, String world) {
        super(renderer, weaponType.imgPath, world);
        this.weaponType = weaponType;
        this.cooldown = weaponType.cooldown;
        this.damage = weaponType.damage;
    }

    /**
     * Attempt to deal damage by yielding the sword
     * @param player the player instance
     * @param direction the direction to show the swinging motion
     */
    public void doDamage(Player player, int direction) {
        ArrayList<BaseEntity> takeDamageList = new ArrayList<>();
        for (BaseEntity actor : renderer.getEntities()) {
            if (actor.pos.subtract(player.pos).magnitude() <= 2) {
                takeDamageList.add(actor);
            }
        }
        for (BaseEntity actor : takeDamageList) {
            actor.takeDamage(damage);
        }
    }
}
