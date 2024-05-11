package Redfoot;

public enum WeaponEnum {
    BROADSWORD(30, 1.4, "BroadSword.png"),
    SILVERSWORD(20, 1, "SilverSword.png"),
    COPPERSWORD(15, 1, "CopperSword.png"),
    FIREBALL(25, 1, "darkFireball.png");

    public final int damage;
    public final double cooldown;
    public final String imgPath;

    WeaponEnum(int damage, double cooldown, String imgPath){
        this.damage = damage;
        this.cooldown = cooldown;
        this.imgPath = imgPath;
    }
}
