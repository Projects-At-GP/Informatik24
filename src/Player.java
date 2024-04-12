import greenfoot.*;
import animator.*;
import vector.Vector2;

public class Player extends BaseEntity{

    int chunkX;
    int chunkY;

    float xInChunk;
    float yInChunk;

    double oldX;
    double oldY;

    final float speed = 4;

    Game game;
    Animation anim;

    public Player(Game game, float x, float y){
        super(null);
        this.game = game;
        this.x = x;
        this.y = y;
        this.isStatic = false;
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
            System.out.println("fire!");
            spell fire = new spell(renderer, (float) (x - 0.5), (float) (y - 0.5));
            game.addObject(fire, 0, 0);
            renderer.entities.add(fire);
            fire.rotate();
        }
    }

    private void movement(float dt){
        float dx = (float) (intIsKeyPressed("d") - intIsKeyPressed("a"));
        float dy = (float) (intIsKeyPressed("s") - intIsKeyPressed("w"));
        double sqrt = Math.sqrt(dx * dx + dy * dy);

        oldX = this.x;
        oldY = this.y;

        if(sqrt != 0) {
            x += dx * speed * dt / (float) sqrt;
            y += dy * speed * dt / (float) sqrt;
            //System.out.printf("Chunk Coordinates x: %d,y: %d; Coordinates in Chunk x: %d, y: %d; PlayerCoordinates X: %d,Y: %d\n", chunkX, chunkY, (int) xInChunk, (int) yInChunk, (int) x, (int) y);
            anim.resume();

            if(dx > 0) {
                anim.setAnim(2);
            } else if (dx < 0) {
                anim.setAnim(1);
            } else if (dy > 0) {
                anim.setAnim(0);
            } else if (dy < 0) {
                anim.setAnim(3);
            }
        } else anim.stop();
        if(x < 0) x = 0;
        if(y < 0) y = 0;

        chunkX = (int) Math.floor( x / 16);
        chunkY = (int) Math.floor( y / 16);

        xInChunk = (float) ((x % 16) - 0.5);
        yInChunk = (float) ((y % 16) - 0.5);
    }

    @Override
    protected void entityTick(Game.State state){
        this.anim.update();
    }

    @Override
    protected void onCollision(BaseActor other, Vector2 mtv){
        this.x = oldX;
        this.y = oldY;
    }
}
