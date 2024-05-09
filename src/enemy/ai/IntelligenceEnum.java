package enemy.ai;

public enum IntelligenceEnum {
     // TODO: make enum values specific for every enemy
     NOOB_INTELLIGENCE(7, 2.2, false),
     ORDINARY_INTELLIGENCE(13, 2.5, false),
     PRO_INTELLIGENCE(16, 3, false),
     SWARM_INTELLIGENCE(16, 3, true);

     public final int range;  // range for initial aggression and aggression-weariness
     public final double speed;  // speed multiplier for movement
     public final boolean canAlarm;  // can alert other enemies & can be alerted whilst being out of range of alert

     IntelligenceEnum(int range, double speed, boolean canAlarm) {
          this.range = range;
          this.speed = speed;
          this.canAlarm = canAlarm;
     }
}
