package enemy.ai;

import Redfoot.BaseEnemy;
import Redfoot.Game;
import enemy.ai.Exceptions.NoPathAvailable;
import vector.Vector2;

import java.util.LinkedList;
import java.util.List;


public class EnemyAI {
    protected final IntelligenceEnum intelligence;
    protected int aggressionWeariness;
    protected boolean isAggro = false;
    protected Vector2 playerPosCache = new Vector2(-0x69, -0x69);  // just a placeholder out of range

    public EnemyAI(IntelligenceEnum intelligence) {
        this.intelligence = intelligence;
    }

    public void setPlayerPosCache(Vector2 playerPosCache) {
        this.playerPosCache = playerPosCache;
    }

    protected void resetAggressionWeariness() {
        this.aggressionWeariness = 0;
    }

    /**
     * Only increase aggression weariness if the player is out of range
     */
    public void increaseAggressionWearinessIfApplicable(BaseEnemy self) {
        if (self.pos == null) return;
        if (self.pos.subtract(playerPosCache).magnitude() <= self.enemyAI.intelligence.range) return;
        this.aggressionWeariness++;
        this.reevaluateAggression();
    }

    protected void reevaluateAggression() {
        if (this.aggressionWeariness < 100) return;  // TODO: find good value
        this.resetAggressionWeariness();
        this.isAggro = false;
    }

    /**
     * Automatically aggro on the players position if the player is in range
     */
    public void autoAggro() {
        if (this.isAggro) return;

        // LOS required for initial aggression if not alerted
        //this.isAggro = this.tryAggressionLOS0();  // TODO
    }

    public void aggro(Vector2 targetPos) {
        this.isAggro = true;
        this.resetAggressionWeariness();
        this.setPlayerPosCache(targetPos);
    }


    public void alertToSwarm(BaseEnemy self, List<BaseEnemy> enemies) {
        if (this.intelligence.canAlarm && !self.isDead) {
            for (BaseEnemy enemy : enemies) {
                if (enemy.pos.subtract(self.pos).magnitude() > enemy.enemyAI.intelligence.range) continue;
                if (!enemy.enemyAI.intelligence.canAlarm) continue;
                enemy.getAlerted(this.playerPosCache);
            }
        }
    }

    public boolean chaseIfPossible(BaseEnemy self, Game.State state) {
        if (!this.isAggro || self.isDead) return false;
        LinkedList<Vector2> path;
        try {
            path = Algorithms.getPath(state.game.render.exportToMapData(), self.pos, this.playerPosCache, this.intelligence);
        } catch (NoPathAvailable e) {
            System.out.println(e.getMessage());  // TODO: remove debug message
            return false;
        }
        Vector2 firstCandidate;
        if ((firstCandidate = path.peekFirst()) == null) return false;
        Vector2 direction = firstCandidate.subtract(self.pos);
        System.out.println(self.pos);
        self.pos.x += direction.normalize().x * state.deltaTime;
        self.pos.y += direction.normalize().y * state.deltaTime;
        System.out.println(self.pos);
        System.out.println();
        return true;
    }
}
