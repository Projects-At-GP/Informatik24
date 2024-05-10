package Redfoot;

import animator.Animation;
import vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Questmaster extends BaseNPC {
    List<BaseEnemy[]> spawnList = new ArrayList<>();

    private void populateSentences(){ // needs linebreak every 28 chars. (\\n)
        this.sentences.add("HALLO. ICH BIN DER          \\nSTADTAELTESTE. ICH GEBE DIR \\nQUESTS.");
        this.sentences.add("KOMM EINFACH ZU MIR UND     \\nSPRICH MICH AN. DANN KANN   \\nICH DIR AUFGABEN GEBEN.     ");
        this.sentences.add("FOLGE DEM PFAD IN RICHTUNG  \\nDES SONNENAUFGANGS. WENN DU \\nAUS DER STADT HERAUS KOMMST \\nGEHE GEN SUEDEN. BESIEGE    \\nDORT DREI SPINNEN.");
    }

    private void populateSpawnList(){
        this.spawnList.add(null);
        this.spawnList.add(null);
        this.spawnList.add(new Spider[]{new Spider(this.renderer, new Vector2(43, 39)),
                new Spider(this.renderer, new Vector2(40, 40)),
                new Spider(this.renderer, new Vector2(42, 41))});
    }

    public Questmaster(Renderer renderer) {
        super(renderer);
        populateSentences();
        populateSpawnList();
        this.pos = new Vector2(16, 19);
        this.anim = new Animation("images/skeletonSheet.png", this, 16, 4, 1);
    }

    @Override
    protected void interactable(){
        if (this.counter < this.sentences.size()){
            this.renderer.showText(this.sentences.get(counter), this);
            if(this.spawnList.get(counter) != null){
                for (BaseEnemy enemy : spawnList.get(counter)){
                    this.renderer.game.spawnEntity(enemy);
                }
            }
            counter++;
        } else this.renderer.showText("ICH HABE NICHTS MIT DIR ZU\\nBESPRECHEN.", this);
    }
}
