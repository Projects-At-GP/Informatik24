import greenfoot.GreenfootImage;

public class Tile extends BaseActor{
    int id;
    public Tile(int id){
        System.out.println(id);
        this.id = id;
        if (id == 1){
            GreenfootImage img = new GreenfootImage("Wall.png");
            setImage(img);
        }
    }
}
