package Redfoot;

import animator.Animation;
import dialogue.Text;
import vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class BaseNPC extends BaseEntity {
    Animation anim;
    List<String> sentences = new ArrayList<>();
    long resetTimer;
    final int timeToReset = 10000;
    int counter;

    public BaseNPC(Renderer renderer) {
        super(renderer);
        this.hp = 100;
        this.col = new collider();
        this.col.octagon(0.8, 0.3);
        this.hasCollider = true;
        this.interactable = true;
    }

    @Override
    protected void awake(){
        this.text = new Text(this.renderer.game, null);
    }

    @Override
    protected void start(){
        this.anim.update();
    }

    @Override
    protected void entityTick(Game.State state) {}

    @Override
    protected void interactable(){
        if(System.currentTimeMillis() >= this.resetTimer) counter = 0;
        if (this.counter < this.sentences.size()){
            this.renderer.showText(this.sentences.get(counter), this);
            this.resetTimer = System.currentTimeMillis() + timeToReset;
            counter++;
        } else this.renderer.showText("ICH HABE NICHTS MIT DIR ZU\\nBESPRECHEN.", this);
    }

    @Override
    public void takeDamage(double dmg){}
}