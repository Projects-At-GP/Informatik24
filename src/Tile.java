import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.logging.Logger;

public class Tile extends BaseActor{
    int id;
    int imgscale = 64;

    public int x;
    public int y;

    public boolean walkable = false;

    public Tile(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
        setGraphics();
    }

    private void setGraphics(){
        if (id == 1){
            GreenfootImage img = new GreenfootImage("./images/Wall.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 2){
            GreenfootImage img = new GreenfootImage("./images/Baum.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 3){
            walkable = true;
            GreenfootImage img = new GreenfootImage("./images/Grass.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 4){
            GreenfootImage img = new GreenfootImage("./images/WallFront.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 5){
            GreenfootImage img = new GreenfootImage("./images/WallTop.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 6){
            walkable = true;
            GreenfootImage img = new GreenfootImage("./images/Door.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 7){
            GreenfootImage img = new GreenfootImage("./images/WallTopCorner.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 8){
            walkable = true;
            GreenfootImage img = new GreenfootImage("./images/Floor.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else if (id == 9){
            GreenfootImage img = new GreenfootImage("./images/Chest.png");
            img.scale(imgscale, imgscale);
            setImage(img);
        } else {
            walkable = true;
        }
    }

    public void highlight(){
        int count = 1;
        while (count > 0){
            GreenfootImage img = new GreenfootImage("./images/texture.png");
            img.scale(imgscale, imgscale);
            setImage(img);
            count--;
        }
        //setGraphics();
    }

    @Override
    protected void priorityTick(Game.State state){
        if(Greenfoot.mouseClicked(this)){
            // TODO logic for interactables
        }
    }


    // to override logging when added to world
    @Override
    protected void addedToWorld(World world) {}
}
