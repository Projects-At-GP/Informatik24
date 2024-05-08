import vector.Vector2;

public class UI extends BaseActor{

    public Vector2 pos;
    public boolean active;
    public UI(Renderer renderer) {
        super(renderer);
        this.getImage().setTransparency(0);
        this.active = false;
    }
}
