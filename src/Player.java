import greenfoot.*;
import animator.*;
import vector.Vector2;

public class Player extends BaseEntity{

    int chunkX;
    int chunkY;

    float xInChunk;
    float yInChunk;

    Vector2 oldPos;

    final float speed = 4;

    Game game;
    Animation anim;

    public Player(Game game, Vector2 pos){
        super(null);
        this.game = game;
        this.pos = pos;
        this.col = new collider();
        this.col.octagon(0.8, 0.3);
        this.hasCollider = true;
    }

    @Override
    protected void awake(){
        this.renderer = game.render;
        anim = new Animation("images/playerSheet.png", this, 16, 4, 1);
    }

    @Override
    protected void priorityTick(Game.State state) {
        movement(state.deltaTime);
        combat();
    }

    private void combat(){
        if (Greenfoot.mouseClicked(null) && Greenfoot.getMouseInfo() != null){
            spell fire = new spell(renderer, pos.subtract(new Vector2(0.5, 0.5)));
            game.addObject(fire, 0, 0);
            renderer.entities.add(fire);
            fire.rotate();
        }
    }

    private void movement(float dt){
        Vector2 dv = new Vector2((intIsKeyPressed("d") - intIsKeyPressed("a")), (intIsKeyPressed("s") - intIsKeyPressed("w"))).normalize();

        oldPos = this.pos;

        if(dv.magnitude() != 0) {
            pos = pos.add(dv.scale(speed * dt));
            //System.out.printf("Chunk Coordinates x: %d,y: %d; Coordinates in Chunk x: %d, y: %d; PlayerCoordinates %s\n", chunkX, chunkY, (int) xInChunk, (int) yInChunk, this.pos.toString());
            anim.resume();

            if(dv.x > 0) {
                anim.setAnim(2);
            } else if (dv.x < 0) {
                anim.setAnim(1);
            } else if (dv.y > 0) {
                anim.setAnim(0);
            } else if (dv.y < 0) {
                anim.setAnim(3);
            }
        } else anim.stop();
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
    }

    @Override
    protected void onCollision(BaseActor other, Vector2 mtv){
        System.out.println("collided");
        this.pos = oldPos;
    }
}
