package enemy.ai;

public enum IntelligenceEnum {
     LINE_OF_SIGHT_LVL0(0, false),  // direct line of sight only
     LINE_OF_SIGHT_LVL1(1, false),  // can walk around one corner
     LINE_OF_SIGHT_LVL2(2, false),  // can walk around two corners
     SWARM_INTELLIGENCE(2, true);  // can alert other enemies & can be alerted whilst being out of range of alert

     public final int maxTurns;
     public final boolean canAlarm;

     IntelligenceEnum(int maxTurns, boolean canAlarm) {
          this.maxTurns = maxTurns;
          this.canAlarm = canAlarm;
     }
}
