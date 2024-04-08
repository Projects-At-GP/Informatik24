import greenfoot.GreenfootImage;

public class Player extends BaseActor{

    float x;
    float y;

    int chunkX;
    int chunkY;

    float xInChunk;
    float yInChunk;

    final float speed = 5;

    public Player(float x, float y){
        GreenfootImage img = new GreenfootImage("./images/player.png");
        img.scale(64,64);
        this.setImage(img);

        this.x = x;
        this.y = y;
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
        }
        if(x < 0) x = 0;
        if(y < 0) y = 0;

        chunkX = (int) Math.floor( x / 16);
        chunkY = (int) Math.floor( y / 16);

        xInChunk = x % 16;
        yInChunk = y % 16;

    }
}
