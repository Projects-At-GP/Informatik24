import greenfoot.GreenfootImage;
import animator.*;

public class Player extends BaseActor{

    float x;
    float y;

    int chunkX;
    int chunkY;

    float xInChunk;
    float yInChunk;

    final float speed = 3;

    Game game;
    Renderer render;
    Animation anim;

    public Player(Game game, float x, float y){
        GreenfootImage img = new GreenfootImage("./images/player1.png");
        img.scale(64,64);
        this.setImage(img);
        this.game = game;
        this.x = x;
        this.y = y;

        anim = new Animation("./src/images/playerSheet.png", this, 16, 4, 1);
        anim.setAnim(0);
    }

    @Override
    protected void start(){
        render = game.render;
    }

    @Override
    protected void priorityTick(Game.State state) {

        float dx = (float) (intIsKeyPressed("d") - intIsKeyPressed("a"));
        float dy = (float) (intIsKeyPressed("s") - intIsKeyPressed("w"));
        double sqrt = Math.sqrt(dx * dx + dy * dy);
        if(sqrt != 0) {
            x += dx * speed * state.deltaTime / (float) sqrt;
            y += dy * speed * state.deltaTime / (float) sqrt;
            //System.out.printf("Chunk Coordinates x: %d,y: %d; Coordinates in Chunk x: %d, y: %d; PlayerCoordinates X: %d,Y: %d\n", chunkX, chunkY, (int) xInChunk, (int) yInChunk, (int) x, (int) y);

            Tile col = render.checkCollision();
            if(col != null){
                x -= dx * speed * state.deltaTime / (float) sqrt;
                y -= dy * speed * state.deltaTime / (float) sqrt;

                //System.out.printf("Collision detected at x: %d, y: %d\n", col.x, col.y);
            }
            anim.isRunning = true;

            if(dx > 0) {
                anim.setAnim(2);
            } else if (dx < 0) {
                anim.setAnim(1);
            } else if (dy > 0) {
                anim.setAnim(0);
            } else if (dy < 0) {
                anim.setAnim(3);
            }
        } else anim.isRunning = false;
        if(x < 0) x = 0;
        if(y < 0) y = 0;

        chunkX = (int) Math.floor( x / 16);
        chunkY = (int) Math.floor( y / 16);

        xInChunk = (float) ((x % 16) - 0.5);
        yInChunk = (float) ((y % 16) - 0.5);
    }

    @Override
    protected void entityTick(Game.State state){
        anim.update();
    }
}
