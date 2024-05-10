package enemy.ai;

import Redfoot.BaseEnemy;
import Redfoot.BaseEntity;
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

    public LinkedList<Vector2> retrievePath(BaseEnemy self, Game.State state) {
        try {
            return Algorithms.getPath(state.game.render.exportToMapData(), self.pos, this.playerPosCache, this.intelligence);
        } catch (NoPathAvailable e) {
            return new LinkedList<>();
        }
    }

    public boolean chaseIfPossible(BaseEnemy self, Game.State state) {
        if (!this.isAggro || self.isDead) return false;

        Vector2 firstCandidate;
        if ((firstCandidate = self.path.peekFirst()) == null) return false;
        if (Math.round(self.pos.x) == firstCandidate.x && Math.round(self.pos.y) == firstCandidate.y) {
            self.path.removeFirst();
            if ((firstCandidate = self.path.peekFirst()) == null) return false;
        }

        Vector2 direction = firstCandidate.subtract(self.pos);

        self.pos.x += direction.normalize().x * state.deltaTime * self.enemyAI.intelligence.speed;
        self.pos.y += direction.normalize().y * state.deltaTime * self.enemyAI.intelligence.speed;

        if ((int) direction.x > 0) {
            self.anim.setAnim(2);
        } else if ((int) direction.x < 0) {
            self.anim.setAnim(1);
        } else if ((int) direction.y > 0) {
            self.anim.setAnim(0);
        } else if ((int) direction.y < 0) {
            self.anim.setAnim(3);
        }

        return true;
    }
}
