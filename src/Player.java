public class Player extends BaseActor{

    public Player(){
        this.setImage("./images/player.png");
    }

    @Override
    protected void priorityTick(Game.State state) {
        move(intIsKeyPressed("d")-intIsKeyPressed("a"), intIsKeyPressed("s")-intIsKeyPressed("w"));
        //System.out.printf("Deltatime: %d\n", state.deltaTime);
    }
}
