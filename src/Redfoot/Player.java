package Redfoot;

import animator.Animation;
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.GreenfootSound;
import vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Player extends BaseEntity {

    int chunkX;
    int chunkY;

    float xInChunk;
    float yInChunk;

    Vector2 oldPos;

    final float speed = 3.5f;
    private final int dashCooldown = 2000;

    Game game;
    Animation anim;
    Animation combatAnim;

    public ArrayList<Item> inventory = new ArrayList<>();
    public ArrayList<BaseEntity> killList = new ArrayList<>();
    public int selectedInventoryIndex;
    public boolean unlockedDungeon;

    private double[] cooldownArray = new double[10];
    private int dir; // 0 for north, clockwise after that
    private int oldIndex;
    private Actor animHolder;
    private int animFramesToDo;
    private GreenfootSound stepSound = new GreenfootSound("./sound/steps.mp3");
    private BaseEntity currentInteractable;
    private UIManager uiManager;
    private boolean ePressed;
    private UI[] hearts = new UI[5];
    private GreenfootImage fullHeart = new GreenfootImage("./images/heartfull.png");
    private GreenfootImage halfHeart = new GreenfootImage("./images/hearthalf.png");
    private GreenfootImage emptyHeart = new GreenfootImage("./images/heartempty.png");
    private double timeSinceDash;
    private long lastDash;
    private boolean dashing;


    public Player(Game game, Vector2 pos) {
        super(null, "Overworld");
        this.game = game;
        this.pos = pos;
        this.hp = 100;
        this.col = new collider();
        this.col.octagon(0.8, 0.3);
        this.hasCollider = true;
        this.stepSound.setVolume(0);
        this.stepSound.playLoop();
        this.stepSound.pause();
        fullHeart.scale(48, 48);
        halfHeart.scale(48, 48);
        emptyHeart.scale(48, 48);
    }

    public void pickupItem(Item item) {
        this.inventory.add(item);
        this.renderer.uiManager.setInventory(inventory);
        this.killList.add(item); // to satisfy the lovely questmaster
    }

    public void killedEnemy(BaseEnemy enemy) {
        this.killList.add(enemy);
    }

    @Override
    protected void awake() {
        this.renderer = game.render;
        super.awake();
        this.anim = new Animation("images/Nanjing.png", this, 16, 4, 1);
        this.animHolder = new BaseActor(renderer);
        this.game.addObject(this.animHolder, 800, 450);

        List<String> SwingColors = new ArrayList<>();
        for (WeaponEnum wEnum : WeaponEnum.values()) {
            if (!SwingColors.contains(wEnum.color)) SwingColors.add(wEnum.color);
        }
        this.combatAnim = new Animation("images/swordSwingSheet.png", this.animHolder, 48, 4, 10, SwingColors);
        this.combatAnim.resume();

        this.uiManager = this.renderer.uiManager;
        this.uiManager.setElement(new UI(this.renderer), "InteractIcon", new Vector2(0, 0));
        GreenfootImage img = new GreenfootImage("./images/interact.png");
        img.scale(27, 27);
        this.uiManager.getElement("InteractIcon").setImage(img);
        this.renderer.addParticle(this.uiManager.getElement("InteractIcon"));

        for (int i = 0; i < this.hearts.length; i++) {
            UI heart = new UI(renderer);
            heart.setImage(fullHeart);
            this.hearts[i] = heart;
            this.uiManager.setElement(heart, "heart" + i, new Vector2(26 + i * 52, 26));
            this.uiManager.enableElement("heart" + i);
        }
    }

    @Override
    protected void priorityTick(Game.State state) {
        //System.out.println(this.pos);
        movement(state.deltaTime);
        combat(state);
        inventory();
        interact();
        if (this.animFramesToDo > 0) {
            this.combatAnim.update();
            this.animFramesToDo--;
        } else this.combatAnim.stop();
    }

    private void interact() {
        List<BaseEntity> entityList = new ArrayList<>(renderer.getEntities());
        entityList.removeIf(entity -> !entity.isInteractable);
        entityList.removeIf(entity -> !entity.active);
        entityList.removeIf(entity -> entity.pos.subtract(this.pos).magnitude() >= 2);
        if (!entityList.isEmpty()) currentInteractable = entityList.get(0);
        if (currentInteractable == null) return;
        this.uiManager.enableElement("InteractIcon");
        this.uiManager.getElement("InteractIcon").pos = currentInteractable.pos.add(new Vector2(0.5, -0.5));
        if (currentInteractable.pos.subtract(this.pos).magnitude() >= 3) {
            currentInteractable = null;
            this.uiManager.disableElement("InteractIcon");
            return;
        }
        if (!ePressed && intIsKeyPressed("e") == 1) {
            currentInteractable.interactWith();
        }
        this.ePressed = intIsKeyPressed("e") == 1;
    }

    private void inventory() {
        oldIndex = selectedInventoryIndex;
        for (int i = 1; i < 10; i++) {
            int value = intIsKeyPressed("" + i) * i;
            if (value != 0) {
                selectedInventoryIndex = value - 1;
            }
        }
        if (intIsKeyPressed("0") != 0) {
            selectedInventoryIndex = 9;
        }
        if (oldIndex != selectedInventoryIndex && selectedInventoryIndex < inventory.size()) {
            cooldownArray[selectedInventoryIndex] = ((Weapon) inventory.get(selectedInventoryIndex)).cooldown;
        }
        oldIndex = selectedInventoryIndex;
        renderer.uiManager.setSelectionIndex(selectedInventoryIndex);
    }

    private void combat(Game.State state) {
        for (int i = 0; i < 10; i++) {
            if (cooldownArray[i] <= 0) continue;
            cooldownArray[i] -= state.deltaTime;
        }

        if (Greenfoot.mouseClicked(null) && Greenfoot.getMouseInfo() != null) {
            if (selectedInventoryIndex < inventory.size()) {
                Item selectedItem = inventory.get(selectedInventoryIndex);
                if (selectedItem instanceof Weapon && cooldownArray[selectedInventoryIndex] <= 0) {
                    ((Weapon) selectedItem).doDamage(this, dir);
                    cooldownArray[selectedInventoryIndex] = ((Weapon) selectedItem).cooldown;
                    if (selectedItem.getClass() == Weapon.class) {
                        this.animFramesToDo = combatAnim.frameCount;
                        this.combatAnim.setColor(((Weapon) selectedItem).weaponType.color);
                        this.combatAnim.resetCounter();
                        this.combatAnim.resume();
                    }
                }
            }
        }
    }

    private void movement(float dt) {
        if(intIsKeyPressed("shift") == 1 && System.currentTimeMillis() - lastDash > dashCooldown){
            lastDash = System.currentTimeMillis();
            dashing = true;
        }
        timeSinceDash = System.currentTimeMillis() - lastDash;


        Vector2 dv = new Vector2((intIsKeyPressed("d") - intIsKeyPressed("a")), (intIsKeyPressed("s") - intIsKeyPressed("w"))).normalize();

        oldPos = this.pos;

        if (dv.magnitude() != 0) {
            this.game.playerPath.add(this.pos);
            this.stepSound.play();
            if (!this.stepSound.isPlaying()) {
                this.stepSound.playLoop();
                logger.info("play steps");
            }
            if(timeSinceDash > 300) {
                timeSinceDash = 0;
                dashing = false;
            }
            double t = Math.min(1.0, timeSinceDash / 300);
            double dashFactor = 2 + (-1 * Math.pow(t - 1, 2));

            pos = pos.add(dv.scale(speed * dt * dashFactor));
            //System.out.printf("Redfoot.Chunk Coordinates x: %d,y: %d; Coordinates in Redfoot.Chunk x: %d, y: %d; PlayerCoordinates %s\n", chunkX, chunkY, (int) xInChunk, (int) yInChunk, this.pos.toString());
            anim.resume();
            if (dv.x > 0) {
                anim.setAnim(2);
                dir = 1;
                if (animFramesToDo == 0) this.animHolder.setRotation(0);
            } else if (dv.x < 0) {
                anim.setAnim(1);
                dir = 3;
                if (animFramesToDo == 0) this.animHolder.setRotation(180);
            } else if (dv.y > 0) {
                anim.setAnim(0);
                dir = 2;
                if (animFramesToDo == 0) this.animHolder.setRotation(90);
            } else if (dv.y < 0) {
                anim.setAnim(3);
                dir = 0;
                if (animFramesToDo == 0) this.animHolder.setRotation(270);
            }
        } else {
            this.anim.stop();
            this.stepSound.pause();
        }
        if (pos.x < 0) pos.x = 0;
        if (pos.y < 0) pos.y = 0;

        chunkX = (int) Math.floor(pos.x / 16);
        chunkY = (int) Math.floor(pos.y / 16);

        xInChunk = (float) ((pos.x % 16) - 0.5);
        yInChunk = (float) ((pos.y % 16) - 0.5);
    }

    @Override
    protected void entityTick(Game.State state) {
        if(!this.dashing) this.anim.update();
    }

    @Override
    protected void onCollision(BaseActor other, Vector2 mtv) {
        if (other instanceof BaseEntity) return;
        this.logger.finest("collided with" + other);
        this.pos = oldPos;
    }

    @Override
    public void takeDamage(double dmg) {
        if(dashing) return;
        super.takeDamage(dmg);

        int fullHearts = (int) (this.hp / 20);
        double remainder = this.hp % 20;

        for (UI heart : this.hearts) {
            if (fullHearts > 0) {
                heart.setImage(fullHeart);
                fullHearts--;
            } else if (remainder > 0) {
                heart.setImage(halfHeart);
                remainder = 0;
            } else {
                heart.setImage(emptyHeart);
            }
        }
    }
}
