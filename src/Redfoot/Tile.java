package Redfoot;

import animator.Animation;
import greenfoot.Greenfoot;
import greenfoot.World;
import vector.Vector2;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Tile extends BaseActor {
    int id;  // the id for the tile type
    int imgscale = 64;  // the scale for the images
    Game game;  // the game instance
    Chunk parent;  // the chunk the tile belongs to
    private Animation anim;  // the animation of the tile
    private int animFramesToDo;  // how many animation frames/circles are pending

    private HashMap<Integer, Weapon> lootTable = new HashMap<>();  // the loot table for chests

    public Tile(int id, Vector2 pos, Game game, Chunk parent) {
        super(game.render);
        this.id = id;
        this.pos = pos;
        this.col = new collider();
        this.col.square(1);
        this.game = game;
        this.parent = parent;
        setGraphics();

        if (this.id == 35) {
            populateLootTable();
            this.anim = new Animation("./images/ChestSheet.png", this, 16, 4, 1);
            if (this.game.openChestList.contains(this.pos)) this.anim.setImage(3);
            this.anim.resume();
        } else if (this.id == 80) {
            this.anim = new Animation("./images/TuerAnimationHolz.png", this, 16, 4, 1);
            if (this.game.openDoorList.contains(this.pos)) this.anim.setImage(3);
            this.anim.resume();
        } else if(this.id == 177) {
            this.anim = new Animation("./images/TuerAnimationStein.png", this, 16, 4, 1);
            if (this.game.openDoorList.contains(this.pos)) this.anim.setImage(3);
            this.anim.resume();
        }
    }

    @Override
    protected void awake(){
        super.awake();
        this.renderer = game.render;
    }

    /**
     * Populate the loot table for chests
     */
    private void populateLootTable(){
        Weapon loot1 = new Weapon(this.renderer, WeaponEnum.IRONSWORD, "dungeon");
        loot1.pos = this.pos.add(new Vector2(1, 1));
        this.lootTable.put(1, loot1);

        Weapon loot2 = new Weapon(this.renderer, WeaponEnum.BROADSWORD, "dungeon");
        loot2.pos = this.pos.add(new Vector2(1, 1));
        this.lootTable.put(40, loot2);

        Weapon loot3 = new Weapon(this.renderer, WeaponEnum.SILVERSWORD, "dungeon");
        loot3.pos = this.pos.add(new Vector2(1, 1));
        this.lootTable.put(8, loot3);

        Weapon loot4 = new Weapon(this.renderer, WeaponEnum.SILVERSWORD, "dungeon");
        loot4.pos = this.pos.add(new Vector2(1, 1));
        this.lootTable.put(16, loot4);
    }

    /**
     * Set the graphics of the tile
     */
    private void setGraphics() {
        this.game.setImageByID(id, this, imgscale);
    }

    @Override
    protected void priorityTick(Game.State state) {
        if (Greenfoot.mouseClicked(this)) {
            this.logger.finest(String.format("A mouse bit me at %1$,.0f/%2$,.0f", pos.x, pos.y));
            //this.game.render.showText("CLICKED TILE ID " + id + "\\nPOS: \\$0000FF" + this.pos + "\\$000000 \\nCHUNK POS: " + parent.pos, this);
        }
    }

    @Override
    protected void blockTick(Game.State state) {
        if (this.id == 104) {
            mineDoor();
        } else if (this.id == 35) {
            chest();
        } else if (this.id == 80) {
            door();
        } else if (this.id == 177) {
            door();
        } else if (this.id == 181) {
            pentagram();
        }
    }

    private void pentagram(){
        if (this.renderer.player.unlockedDungeon && this.isTouching(Player.class)) {
            this.game.render.changeWorld("overworld");
        }
    }

    /**
     * Logic for chests if the tile identifies as a door
     */
    private void door() {
        if (this.pos.subtract(this.game.render.player.pos).magnitude() <= 2 && !this.game.openDoorList.contains(this.pos)){
            animFramesToDo = this.anim.frameCount;
            this.game.openDoorList.add(this.pos);
        }
        if (this.animFramesToDo > 0) {
            this.anim.update();
            this.animFramesToDo--;
        }
    }

    /**
     * Logic for chests if the tile identifies as a door to the mines/dungeon
     */
    private void mineDoor() {
        if (this.renderer.player.unlockedDungeon && this.isTouching(Player.class)) {
            this.game.render.changeWorld("dungeon");
        }
    }

    /**
     * Logic for chests if the tile identifies as a chest
     */
    private void chest() {
        if (this.pos.subtract(this.game.render.player.pos).magnitude() <= 2 && !this.game.openChestList.contains(this.pos)) {
            logger.info("player in range");
            animFramesToDo = this.anim.frameCount;
            this.game.openChestList.add(this.pos);
            this.game.spawnEntity(this.lootTable.get((int) this.pos.x));
        }
        if (this.animFramesToDo > 0) {
            this.anim.update();
            this.animFramesToDo--;
        }
    }

    /**
     * Indicator whether the tile is walkable or not
     */
    public boolean isWalkable() {
        return !this.hasCollider;
    }

    /**
     * to override logging when added to world
     */
    @Override
    protected void addedToWorld(World world) {
        this.logger = Logger.getLogger(this.getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return String.format("Redfoot.Tile ID %d with %s Redfoot.collider at position %s", this.id, hasCollider ? "a" : "no", pos.toString());
    }
}
