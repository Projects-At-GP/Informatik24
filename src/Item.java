import animator.Animation;
import greenfoot.GreenfootImage;
import vector.Vector2;

import java.io.File;

public class Item extends BaseEntity{
    public GreenfootImage img;
    private final String filePrefix = (new File("./src/")).exists()? "./src/" : "./";
    public Item(Renderer renderer, String imgPath) {
        super(renderer);
        this.img =new GreenfootImage(filePrefix + "images/weapons/" + imgPath);
        this.pos = new Vector2(20.5, 20.5);
    }

    @Override
    protected void awake(){
        this.img.scale(64,64);
        this.setImage(img);
    }

    @Override
    protected void entityTick(Game.State state){
        if(this.isTouching(Player.class)){
            System.out.println("Player picked up Item!");
            renderer.player.pickupItem(this);
            renderer.entities.remove(this);
            renderer.game.removeObject(this);
        }
    }

    // should not be needed as items do not have colliders but just to be sure
    @Override
    public void takeDamage(double dmg) {}
}
