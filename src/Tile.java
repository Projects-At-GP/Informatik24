import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.logging.Logger;

public class Tile extends BaseActor{
    int id;
    int imgscale = 64;

    public int x;
    public int y;

    public boolean wakable = false;

    public Tile(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
        if (id == 1){
            GreenfootImage img = new GreenfootImage("./images/Wall.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 2){
            GreenfootImage img = new GreenfootImage("./images/Baum.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 3){
            wakable = true;
            GreenfootImage img = new GreenfootImage("./images/Grass.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else {
            wakable = true;
        }
    }

    protected void addedToWorld(World world) {}
}
