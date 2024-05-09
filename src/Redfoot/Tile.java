package Redfoot;

import animator.Animation;
import greenfoot.Greenfoot;
import greenfoot.World;
import vector.Vector2;

import java.util.logging.Logger;

public class Tile extends BaseActor{
    int id;
    int imgscale = 64;
    Game game;
    Chunk parent;
    private Animation anim;
    private int animFramesToDo;
    private boolean open = false;


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
    protected void awake(){
        if(this.id == 1 || this.id == 21 || this.id == 22 || this.id == 83) {
            this.anim = new Animation("./images/ChestSheet.png", this, 16, 4, 1);
            this.anim.resume();
        } else if(this.id == 79){
            this.anim = new Animation("./images/TorchSheet.png", this, 16, 4, 1);
            this.anim.resume();
        }
    }

    @Override
    protected void priorityTick(Game.State state){
        if(Greenfoot.mouseClicked(this)){
            // TODO logic for interactables
            System.out.println(this.pos);
            //this.game.render.showText("CLICKED TILE ID " + id + "\\nPOS: " + this.pos + "\\nCHUNK POS: " + parent.pos, this);
        }
    }

    @Override
    protected void blockTick(Game.State state){
        if (this.id == 16){
            mineDoor();
        } else if(this.id == 1 || this.id == 21 || this.id == 22 || this.id == 83) {
            chest();
        } else if(this.id == 79){
            torch();
        }
    }

    private void torch(){
        this.anim.update();
    }

    private void mineDoor(){
        if (this.isTouching(Player.class)){
            this.game.render.changeWorld("dungeon");
        }
    }

    private void chest(){
        if (this.pos.subtract(this.game.render.player.pos).magnitude() <= 2){
            logger.info("player in range");
            if (!open){
                animFramesToDo = this.anim.frameCount;
                open = true;
            }
        }
        if(this.animFramesToDo > 0){ // TODO make chests an entity so that they do not get reloaded when crossing chunks
            this.anim.update();
            this.animFramesToDo--;
        }
    }

    public boolean isWalkable() {
        return !this.hasCollider;
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
