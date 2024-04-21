import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;
import vector.Vector2;

import java.io.File;
import java.util.logging.Logger;

public class Tile extends BaseActor{
    int id;
    int imgscale = 64;
    Game game;

    public boolean walkable = false;

    public Tile(int id, Vector2 pos, Game game){
        super(null);
        this.id = id;
        this.pos = pos;
        this.col = new collider();
        this.col.square(1);
        this.game = game;
        setGraphics();
    }

    private void setGraphics(){
        game.setImageByID(id, this, imgscale);
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
