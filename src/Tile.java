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
        super(game.render);
        this.id = id;
        this.pos = pos;
        this.col = new collider();
        this.col.square(1);
        this.game = game;
        setGraphics();
    }

    private void setGraphics(){
        this.game.setImageByID(id, this, imgscale);
    }

    @Override
    protected void priorityTick(Game.State state){
        if(Greenfoot.mouseClicked(this)){
            // TODO logic for interactables
            System.out.println(this.pos);
            this.game.render.showText("CLICKED TILE ID " + id, this);
        }
    }


    // to override logging when added to world
    @Override
    protected void addedToWorld(World world) {
        this.logger = Logger.getLogger(this.getClass().getSimpleName());
    }
}
