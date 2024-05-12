package Redfoot;

public enum WeaponEnum {
    BROADSWORD(30, 1.4, "BroadSword.png", "606060"),
    SILVERSWORD(20, 1, "SilverSword.png", "Default"),
    COPPERSWORD(15, 1, "CopperSword.png", "E19343"),
    FIREBALL(25, 1, "Fireball.png", "Default");

    public final int damage;
    public final double cooldown;
    public final String imgPath;
    public final String color;

    WeaponEnum(int damage, double cooldown, String imgPath, String color) {
        this.damage = damage;
        this.cooldown = cooldown;
        this.imgPath = imgPath;
        this.color = color;
    }
}
