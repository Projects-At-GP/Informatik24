package Redfoot;

import dialogue.Text;
import greenfoot.*;
import animator.*;
import vector.Vector2;

import java.util.ArrayList;

public class Player extends BaseEntity{

    int chunkX;
    int chunkY;

    float xInChunk;
    float yInChunk;

    Vector2 oldPos;

    final float speed = 4;

    Game game;
    Animation anim;
    Animation combatAnim;
    public ArrayList<Item> inventory = new ArrayList<>();
    private double[] cooldownArray = new double[10];
    public int selectedInventoryIndex;
    private int dir; // 0 for north, clockwise after that
    private int oldIndex;
    private Actor animHolder;
    private int animFramesToDo;
    private GreenfootSound stepSound = new GreenfootSound("./sound/steps.mp3");



    public Player(Game game, Vector2 pos){
        super(null);
        this.game = game;
        this.pos = pos;
        this.col = new collider();
        this.col.octagon(0.8, 0.3);
        this.hasCollider = true;
        this.stepSound.setVolume(0);
        this.stepSound.playLoop();
        this.stepSound.pause();
    }

    public void pickupItem(Item item){
        this.inventory.add(item);
        this.renderer.uiManager.setInventory(inventory);
    }

    @Override
    protected void awake(){
        this.renderer = game.render;
        this.anim = new Animation("images/playerSheet.png", this, 16, 4, 1);
        this.animHolder = new BaseActor(renderer);
        this.game.addObject(this.animHolder, 800, 450);
        this.combatAnim = new Animation("images/swordSwingSheet.png", this.animHolder, 48, 4, 7);
        this.combatAnim.resume();
    }

    @Override
    protected void priorityTick(Game.State state) {
        movement(state.deltaTime);
        combat(state);
        oldIndex = selectedInventoryIndex;
        for (int i = 1; i < 10; i++){
            int value = intIsKeyPressed("" + i) * i;
            if (value != 0){
                selectedInventoryIndex = value - 1;
            }
        }
        if (intIsKeyPressed("0") != 0){
            selectedInventoryIndex = 9;
        }
        if (oldIndex != selectedInventoryIndex && selectedInventoryIndex < inventory.size()){
            cooldownArray[selectedInventoryIndex] = ((Weapon) inventory.get(selectedInventoryIndex)).cooldown;
        }
        oldIndex = selectedInventoryIndex;
        renderer.uiManager.setSelectionIndex(selectedInventoryIndex);
    }

    private void combat(Game.State state){
        for (int i = 0; i < 10; i++){
            if(cooldownArray[i] <= 0) continue;
            cooldownArray[i] -= state.deltaTime;
        }

        if (Greenfoot.mouseClicked(null) && Greenfoot.getMouseInfo() != null){
            if(selectedInventoryIndex < inventory.size()){
                Item selectedItem = inventory.get(selectedInventoryIndex);
                if (selectedItem instanceof Weapon && cooldownArray[selectedInventoryIndex] <= 0){
                    ((Weapon) selectedItem).doDamage(this, dir);
                    cooldownArray[selectedInventoryIndex] = ((Weapon) selectedItem).cooldown;
                    if (selectedItem.getClass() == Weapon.class){
                        this.animFramesToDo = combatAnim.frameCount;
                        this.combatAnim.resetCounter();
                        this.combatAnim.resume();
                    }
                }
            }
        }
    }

    private void movement(float dt){
        Vector2 dv = new Vector2((intIsKeyPressed("d") - intIsKeyPressed("a")), (intIsKeyPressed("s") - intIsKeyPressed("w"))).normalize();

        oldPos = this.pos;
        
        if(dv.magnitude() != 0) {
            this.stepSound.play();
            if(!this.stepSound.isPlaying()){
                this.stepSound.playLoop();
                logger.info("play steps");
            }
            pos = pos.add(dv.scale(speed * dt));
            //System.out.printf("Redfoot.Chunk Coordinates x: %d,y: %d; Coordinates in Redfoot.Chunk x: %d, y: %d; PlayerCoordinates %s\n", chunkX, chunkY, (int) xInChunk, (int) yInChunk, this.pos.toString());
            anim.resume();
            if(dv.x > 0) {
                anim.setAnim(2);
                dir = 1;
                if(animFramesToDo == 0) this.animHolder.setRotation(0);
            } else if (dv.x < 0) {
                anim.setAnim(1);
                dir = 3;
                if(animFramesToDo == 0) this.animHolder.setRotation(180);
            } else if (dv.y > 0) {
                anim.setAnim(0);
                dir = 2;
                if(animFramesToDo == 0) this.animHolder.setRotation(90);
            } else if (dv.y < 0) {
                anim.setAnim(3);
                dir = 0;
                if(animFramesToDo == 0) this.animHolder.setRotation(270);
            }
        } else {
            this.anim.stop();
            this.stepSound.pause();
        }
        if(pos.x < 0) pos.x = 0;
        if(pos.y < 0) pos.y = 0;

        chunkX = (int) Math.floor( pos.x / 16);
        chunkY = (int) Math.floor( pos.y / 16);

        xInChunk = (float) ((pos.x % 16) - 0.5);
        yInChunk = (float) ((pos.y % 16) - 0.5);
    }

    @Override
    protected void entityTick(Game.State state){
        this.anim.update();
        if (this.animFramesToDo > 0){
            this.combatAnim.update();
            this.animFramesToDo--;
        } else this.combatAnim.stop();
    }

    @Override
    protected void onCollision(BaseActor other, Vector2 mtv){
        System.out.println("collided with" + other);
        if(other instanceof BaseEntity) return;
        this.pos = oldPos;
    }
}
