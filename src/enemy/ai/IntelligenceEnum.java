package enemy.ai;

public enum IntelligenceEnum {
     NOOB_INTELLIGENCE(7, false),
     ORDINARY_INTELLIGENCE(13, false),
     PRO_INTELLIGENCE(16, false),
     SWARM_INTELLIGENCE(16, true);  // can alert other enemies & can be alerted whilst being out of range of alert

     public final int range;
     public final boolean canAlarm;

     IntelligenceEnum(int range, boolean canAlarm) {
          this.range = range;
          this.canAlarm = canAlarm;
     }
}
