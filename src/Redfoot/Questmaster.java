package Redfoot;

import animator.Animation;
import vector.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Questmaster extends BaseNPC {
    List<BaseEnemy[]> spawnList = new ArrayList<>();

    private void populateSentences() { // needs linebreak every 28 chars. (\\n)
        this.sentences.add("HALLO. ICH BIN DER          \\nSTADTAELTESTE. ICH GEBE DIR \\nQUESTS.");
        this.sentences.add("KOMM EINFACH ZU MIR UND     \\nSPRICH MICH AN. DANN KANN   \\nICH DIR AUFGABEN GEBEN.     ");
        this.sentences.add("FOLGE DEM PFAD IN RICHTUNG  \\nDES SONNENAUFGANGS. WENN DU \\nAUS DER STADT HERAUS KOMMST \\nGEHE GEN SUEDEN. BESIEGE    \\nDORT DREI SPINNEN.");
        this.sentences.add("HERZLICHEN GLUECKWUNSCH. DU \\nHAST MEINE AUFGABE ERLEDIGT.\\nVIELEN DANK, DAS HAT DER    \\nDER STADT GEHOLFEN!");
        this.sentences.add("UEBERASCHUNGSANGRIFF!!!!    \\$00FF00 \\nBLOBS!!!");
        this.sentences.add("HEHEHEHEH");
        this.sentences.add("SO? WO WAR ICH?...");
        this.sentences.add("ACHJA... DU MUSST MIR HELFEN");
    }

    private void populateSpawnList() {
        this.spawnList.add(null);
        this.spawnList.add(null);
        this.spawnList.add(new Spider[]{new Spider(this.renderer, new Vector2(43, 39), "overworld"),
                new Spider(this.renderer, new Vector2(40, 40), "overworld"),
                new Spider(this.renderer, new Vector2(42, 41), "overworld")});
        this.spawnList.add(null);
        this.spawnList.add(new Blob[]{new Blob(this.renderer, new Vector2(20, 20), "overworld"),
                new Blob(this.renderer, new Vector2(22, 21), "overworld")});
        this.spawnList.add(null);
        this.spawnList.add(null);
        this.spawnList.add(null);
    }

    public Questmaster(Renderer renderer, String world) {
        super(renderer, world);
        populateSentences();
        populateSpawnList();
        this.pos = new Vector2(46, 31);
        this.anim = new Animation("images/skeletonSheet.png", this, 16, 4, 1);
    }

    @Override
    protected void interactWith() {
        boolean finished = false;
        if (counter > 0) {
            if (this.spawnList.get(counter - 1) != null) {
                if (this.renderer.player.killList.containsAll(Arrays.asList(this.spawnList.get(counter - 1)))) {
                    finished = true;
                }
            } else finished = true;
        } else finished = true;
        if (this.counter < this.sentences.size() && finished) {
            this.renderer.showText(this.sentences.get(counter), this);
            if (this.spawnList.get(counter) != null) {
                for (BaseEnemy enemy : spawnList.get(counter)) {
                    this.renderer.game.spawnEntity(enemy);
                }
            }
            counter++;
        } else if (!finished) {
            this.renderer.showText(this.sentences.get(counter - 1), this);
        } else this.renderer.showText("ICH HABE NICHTS MIT DIR ZU\\nBESPRECHEN.", this);
    }
}
