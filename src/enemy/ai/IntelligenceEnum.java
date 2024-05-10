package enemy.ai;

public enum IntelligenceEnum {
     ABSENT_IQ(0, 0, 0, false),
     SPIDER(5, 8.5, 3, false);

     public final double aggressionRange;  // range for initial aggression
     public final double chasingRange;  // range for chasing
     public final double speed;  // speed multiplier for movement
     public final boolean canAlarm;  // can alert other enemies & can be alerted whilst being out of range of alert

     IntelligenceEnum(double aggressionRange, double chasingRange, double speed, boolean canAlarm) {
         this.aggressionRange = aggressionRange;
         this.chasingRange = chasingRange;
         this.speed = speed;
         this.canAlarm = canAlarm;
     }
}
