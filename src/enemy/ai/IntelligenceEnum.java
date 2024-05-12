package enemy.ai;

/**
 * Enum to hold information about the individual enemies.
 */
public enum IntelligenceEnum {
    ABSENT_IQ(0, 0, 0, 0, 0, 0x1337, 0, 0, false),
    SPIDER(5, 8.5, 3, 0.75, 10, 45, 3, 1.5, false),
    BLOB(6.5, 11, 4, 1, 20, 30, 3, 1, false);


    public final double aggressionRange;  // range for initial aggression
    public final double chasingRange;  // range for chasing
    public final double wanderRange;  // range to wander in (centered on spawn of enemy)
    public final double attackRange;  // range to deal damage
    public final int attackDamage;  // damage to deal
    public final int attackCooldownTicks;  // ticks to wait until more damage is dealt
    public final double speed;  // speed multiplier for movement
    public final double wanderSpeed;  // speed multiplier for wandering
    public final boolean canAlarm;  // can alert other enemies & can be alerted whilst being out of range of alert

    IntelligenceEnum(double aggressionRange, double chasingRange, double wanderRange, double attackRange, int attackDamage, int attackCooldownTicks, double speed, double wanderSpeed, boolean canAlarm) {
        this.aggressionRange = aggressionRange;
        this.chasingRange = chasingRange;
        this.wanderRange = wanderRange;
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        this.attackCooldownTicks = attackCooldownTicks;
        this.speed = speed;
        this.wanderSpeed = wanderSpeed;
        this.canAlarm = canAlarm;
    }
}
