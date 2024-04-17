import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;
import vector.Vector2;

import java.io.File;
import java.util.logging.Logger;

public class Tile extends BaseActor{
    int id;
    int imgscale = 64;

    public boolean walkable = false;

    public Tile(int id, Vector2 pos){
        super(null);
        this.id = id;
        this.pos = pos;
        this.col = new collider();
        this.col.square(1);
        setGraphics();
    }

    private void setGraphics(){
        File file = new File("./src/images/textures/");
        File[] dir = file.listFiles();

        if(dir == null) return;
        if (id >= 0 && id < dir.length){
            GreenfootImage img = new GreenfootImage(dir[id].getPath());
            img.scale(imgscale, imgscale);
            setImage(img);
            String[] keys = dir[id].getName().replace(".png", "").split("-");
            if(keys[1].equals("N")) this.hasCollider = true;
        }
    }

    @Override
    protected void priorityTick(Game.State state){
        if(Greenfoot.mouseClicked(this)){
            // TODO logic for interactables
        }
    }


    // to override logging when added to world
    @Override
    protected void addedToWorld(World world) {
        this.logger = Logger.getLogger(this.getClass().getSimpleName());
    }
}
