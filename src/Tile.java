import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.logging.Logger;

public class Tile extends BaseActor{
    int id;
    int imgscale = 64;
    public Tile(int id){
        this.id = id;
        if (id == 1){
            GreenfootImage img = new GreenfootImage("./images/Wall.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        }
        if (id == 2){
            GreenfootImage img = new GreenfootImage("./images/Baum.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        }
        if (id == 3){
            GreenfootImage img = new GreenfootImage("./images/Grass.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        }
    }

    protected void addedToWorld(World world) {}
}
