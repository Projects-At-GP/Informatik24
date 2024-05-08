package enemy.ai;

public enum Intelligence {
     LINE_OF_SIGHT_LVL0,  // direct line of sight only
     LINE_OF_SIGHT_LVL1,  // can see around one corner
     LINE_OF_SIGHT_LVL2,  // can see around two corners
     SWARM_INTELLIGENCE,  // can alert other enemies & can be alerted whilst being out of range of alert
}
