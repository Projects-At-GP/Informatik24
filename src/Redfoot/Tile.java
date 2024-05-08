package Redfoot;

import greenfoot.Greenfoot;
import greenfoot.World;
import vector.Vector2;

import java.util.logging.Logger;

public class Tile extends BaseActor{
    int id;
    int imgscale = 64;
    Game game;
    Chunk parent;

    public boolean walkable = false;

    public Tile(int id, Vector2 pos, Game game, Chunk parent){
        super(game.render);
        this.id = id;
        this.pos = pos;
        this.col = new collider();
        this.col.square(1);
        this.game = game;
        this.parent = parent;
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
            //this.game.render.showText("CLICKED TILE ID " + id + "\\nPOS: " + this.pos + "\\nCHUNK POS: " + parent.pos, this);
        }
        if (this.id == 16){
            mineDoor();
        }
    }

    private void mineDoor(){
        if (this.isTouching(Player.class)){
            this.game.render.changeWorld("dungeon");
        }
    }


    // to override logging when added to world
    @Override
    protected void addedToWorld(World world) {
        this.logger = Logger.getLogger(this.getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return String.format("Redfoot.Tile ID %d with %s Redfoot.collider at position %s", this.id, hasCollider? "a" : "no", pos.toString());
    }
}
