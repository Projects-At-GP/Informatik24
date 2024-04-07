import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.logging.Logger;

public class Tile extends BaseActor{
    int id;
    public Tile(int id){
        this.id = id;
        if (id == 1){
            GreenfootImage img = new GreenfootImage("./images/Wall.png");
            img.scale(64, 64);
            setImage(img);
        }
    }

    protected void addedToWorld(World world) {}
}
