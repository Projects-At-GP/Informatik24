public class Player extends BaseActor{

    float x;
    float y;

    public Player(float x, float y){
        this.setImage("./images/player.png");
        this.x = x;
        this.y = y;
    }

    @Override
    protected void priorityTick(Game.State state) {
        x += (float) (intIsKeyPressed("d") - intIsKeyPressed("a")) /10;
        y += (float) (intIsKeyPressed("s") - intIsKeyPressed("w")) /10;
    }
}
