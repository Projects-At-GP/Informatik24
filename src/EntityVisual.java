import greenfoot.*;

public class EntityVisual extends Actor {

    float x;
    float y;

    int screenX;
    int screenY;

    public EntityVisual(float x, float y){
        this.x = x;
        this.y = y;
        GreenfootImage img = new GreenfootImage(64, 64);
        img.setTransparency(0);
        this.setImage(img);
    }

    public EntityVisual(int x, int y){
        this.screenX = x;
        this.screenY = y;
        GreenfootImage img = new GreenfootImage(64, 64);
        img.setTransparency(0);
        this.setImage(img);
    }
}
