package enemy.ai;

import vector.Vector2;

import java.util.List;


public class EnemyAI {
    protected final IntelligenceEnum intelligence;
    public final int aggressionRange;

    protected int aggressionWeariness;
    protected boolean isAggro = false;
    protected Vector2 playerPosCache = new Vector2(-0x69d, -0x69);  // just a placeholder out of range

    public EnemyAI(IntelligenceEnum intelligence, int aggressionRange) {
        this.intelligence = intelligence;
        this.aggressionRange = aggressionRange;
    }

    public void setPlayerPosCache(Vector2 playerPosCache) {
        this.playerPosCache = playerPosCache;
    }

    protected void resetAggressionWeariness() {
        this.aggressionWeariness = 0;
    }

    /**
     * Only increase aggression weariness if the player can't be seen
     */
    public void increaseAggressionWearinessIfApplicable() {
        switch (this.intelligence) {
            case LINE_OF_SIGHT_LVL0:
                if (this.tryAggressionLOS0()) return;
            case LINE_OF_SIGHT_LVL1:
                if (this.tryAggressionLOS1()) return;
            case LINE_OF_SIGHT_LVL2:
            case SWARM_INTELLIGENCE:
                if (this.tryAggressionLOS2()) return;
        }
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
        this.isAggro = this.tryAggressionLOS0();
    }

    public void aggro(Vector2 targetPos) {
        this.isAggro = true;
        this.resetAggressionWeariness();
        this.setPlayerPosCache(targetPos);
    }

    /**
     * Check if aggression would be possible for LineOfSight of level 0
     * @return whether aggression would be possible
     */
    protected boolean tryAggressionLOS0() {
        return false;  // TODO
    }

    /**
     * Check if aggression would be possible for LineOfSight of level 1
     * @return whether aggression would be possible
     */
    protected boolean tryAggressionLOS1() {
        if (this.tryAggressionLOS0()) return true;
        return false;  // TODO
    }

    /**
     * Check if aggression would be possible for LineOfSight of level 2
     * @return whether aggression would be possible
     */
    protected boolean tryAggressionLOS2() {
        if (this.tryAggressionLOS1()) return true;
        return false;  // TODO
    }

    public void alertToSwarm(BaseEnemyInterface self, List<BaseEnemyInterface> enemies) {
        if (this.intelligence.equals(IntelligenceEnum.SWARM_INTELLIGENCE)) {
            for (BaseEnemyInterface enemy : enemies) {
                if (enemy.getPos().subtract(self.getPos()).magnitude() > enemy.getAI().aggressionRange) continue;
                if (!enemy.getAI().intelligence.equals(IntelligenceEnum.SWARM_INTELLIGENCE)) continue;
                enemy.getAlerted(this.playerPosCache);
            }
        }
    }

    public void chaseIfPossible() {
        if (!this.isAggro) return;
        // TODO
    }
}
