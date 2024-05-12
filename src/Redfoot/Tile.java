package Redfoot;

import animator.Animation;
import greenfoot.Greenfoot;
import greenfoot.World;
import vector.Vector2;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Tile extends BaseActor {
    int id;
    int imgscale = 64;
    Game game;
    Chunk parent;
    private Animation anim;
    private int animFramesToDo;

    private HashMap<Integer, Weapon> lootTable = new HashMap<Integer, Weapon>();

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
            this.anim = new Animation("./images/TürAnimationHolz.png", this, 16, 4, 1);
            if (this.game.openDoorList.contains(this.pos)) this.anim.setImage(3);
            this.anim.resume();
        } else if(this.id == 177) {
            this.anim = new Animation("./images/TürAnimationStein.png", this, 16, 4, 1);
            if (this.game.openDoorList.contains(this.pos)) this.anim.setImage(3);
            this.anim.resume();
        }
    }

    @Override
    protected void awake(){
        super.awake();
        this.renderer = game.render;
    }

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

    private void mineDoor() {
        if (this.renderer.player.unlockedDungeon && this.isTouching(Player.class)) {
            this.game.render.changeWorld("dungeon");
        }
    }

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
        return String.format("Redfoot.Tile ID %d with %s Redfoot.collider at position %s", this.id, hasCollider ? "a" : "no", pos.toString());
    }
}
