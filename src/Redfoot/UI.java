package Redfoot;

/**
 * Used to hold elements from the user interface
 */
public class UI extends BaseActor {
    public boolean active;  // whether this element is active or not

    public UI(Renderer renderer) {
        super(renderer);
        this.getImage().setTransparency(0);
        this.active = false;
    }
}
