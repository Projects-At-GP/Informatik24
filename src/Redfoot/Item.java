package Redfoot;

import greenfoot.GreenfootImage;

import java.io.File;

public class Item extends BaseEntity {
    public GreenfootImage img;
    private final String filePrefix = (new File("./src/")).exists() ? "./src/" : "./";

    public Item(Renderer renderer, String imgPath) {
        super(renderer);
        this.img = new GreenfootImage(filePrefix + "images/weapons/" + imgPath);
    }

    @Override
    protected void awake() {
        super.awake();
        this.img.scale(64, 64);
        this.setImage(img);
    }

    /**
     * Let the player pick an item up if it touches it
     */
    @Override
    protected void entityTick(Game.State state) {
        if (this.isTouching(Player.class)) {
            this.logger.info(String.format("picked up following item: %s", this));
            renderer.player.pickupItem(this);
            renderer.ceaseEntity(this);
            renderer.game.removeObject(this);
        }
    }

    /**
     * Make Items invincible; shouldn't be necessary though as items don't have colliders -> just in case
     */
    @Override
    public void takeDamage(double dmg) {
    }
}
