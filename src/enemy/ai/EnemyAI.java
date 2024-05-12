package enemy.ai;

import Redfoot.BaseEnemy;
import Redfoot.Game;
import enemy.ai.Exceptions.NoPathAvailable;
import vector.Vector2;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Class to outsource internal logic for enemy movement.
 * Call hierarchy is still preserved in the BaseEnemy. BaseEnemy will only tell the order of operations.
 */
public class EnemyAI {
    public final IntelligenceEnum intelligence;
    public boolean isAggro = false;
    public Vector2 playerPosCache = new Vector2(-0x69, -0x69);  // just a placeholder out of range
    private double dealDamageAfterTick = 0;  // used for cooldown -> don't kill the player in a matter of just a few ticks

    public EnemyAI(IntelligenceEnum intelligence) {
        this.intelligence = intelligence;
    }

    /**
     * Used to cache the players position as the information is only propagated through tick-methods
     *
     * @param playerPosCache the current position
     */
    public void setPlayerPosCache(Vector2 playerPosCache) {
        this.playerPosCache = playerPosCache;
    }

    /**
     * Check whether the player is still in chasing range
     *
     * @param self the enemy the AI-instance belongs to
     */
    public void reevaluateAggression(BaseEnemy self) {
        this.isAggro = self.pos.subtract(playerPosCache).magnitude() <= self.enemyAI.intelligence.chasingRange;
    }

    /**
     * Automatically aggro on the players position if the player is in range
     *
     * @return true if aggression was activated during the method call
     */
    public boolean autoAggro(BaseEnemy self) {
        if (this.isAggro) return false;
        this.isAggro = self.pos.subtract(playerPosCache).magnitude() <= self.enemyAI.intelligence.aggressionRange;
        return this.isAggro;
    }

    /**
     * Makes the enemy aggro on the targeted position
     *
     * @param targetPos the position to target (to set playerPosCache to)
     */
    public void aggro(Vector2 targetPos) {
        this.isAggro = true;
        this.setPlayerPosCache(targetPos);
    }

    /**
     * Alert other enemies via the magic of swarm intelligence (only if the calling enemy is intelligent itself)
     *
     * @param self    the enemy the AI-instance belongs to
     * @param enemies a list of every enemy to potentially alert
     */
    public void alertToSwarm(BaseEnemy self, List<BaseEnemy> enemies) {
        if (!self.enemyAI.isAggro || !this.intelligence.canAlarm || self.isDead) return;
        for (BaseEnemy enemy : enemies) {
            if (enemy.enemyAI.isAggro) continue;
            if (enemy.pos.subtract(self.pos).magnitude() > enemy.enemyAI.intelligence.chasingRange) continue;
            if (!enemy.enemyAI.intelligence.canAlarm) continue;
            enemy.getAlerted(this.playerPosCache);
        }
    }

    /**
     * Retrieve a path which either chases the player or wanders in a radius around the spawn of the enemy
     *
     * @param self  the enemy the AI-instance belongs to
     * @param state the current Game.State object from a tick
     * @return a walkable path
     */
    public LinkedList<Vector2> retrievePath(BaseEnemy self, Game.State state) {
        LinkedList<Vector2> path;
        try {
            path = Algorithms.getPath(state.game.render.exportToMapData(), self.pos, this.playerPosCache, this.intelligence, self.bypassesChaseRadius(state.tick));
            self.isWandering = false;
        } catch (NoPathAvailable e) {
            if (self.spawnedAt == null) return new LinkedList<>();

            // mathematical implementation based on https://programming.guide/random-point-within-circle.html
            double a = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
            double r = intelligence.wanderRange * Math.sqrt(ThreadLocalRandom.current().nextDouble());
            Vector2 targetCandidate = self.spawnedAt.add(new Vector2(r * Math.cos(a), r * Math.sin(a)));
            try {
                path = Algorithms.getPath(state.game.render.exportToMapData(), self.pos, targetCandidate, this.intelligence, true);
                self.isWandering = true;
            } catch (NoPathAvailable ex) {
                return new LinkedList<>();
            }
        }
        return path;
    }

    /**
     * Tries to chase the player or in case of failure it will try to wander around their spawn
     *
     * @param self  the enemy the AI-instance belongs to
     * @param state the current Game.State object from a tick
     * @return true if the enemy was able to move (either chased or wandered)
     */
    public boolean chaseOrWanderIfPossible(BaseEnemy self, Game.State state) {
        if (!(this.isAggro || self.isWandering) || self.isDead) return false;

        Vector2 firstCandidate;
        if ((firstCandidate = self.path.peekFirst()) == null) return false;
        if (Math.round(self.pos.x) == firstCandidate.x && Math.round(self.pos.y) == firstCandidate.y) {
            self.path.removeFirst();
            if ((firstCandidate = self.path.peekFirst()) == null) return false;
        }

        Vector2 direction = firstCandidate.subtract(self.pos);

        double speed = self.isWandering ? self.enemyAI.intelligence.wanderSpeed : self.enemyAI.intelligence.speed;

        self.pos.x += direction.normalize().x * state.deltaTime * speed;
        self.pos.y += direction.normalize().y * state.deltaTime * speed;

        setAnimation(self, direction);
        return true;
    }

    /**
     * Tries to damage the player
     *
     * @param self  the enemy the AI-instance belongs to
     * @param state the current Game.State object from a tick
     * @return true if the enemy was able to damage the player
     */
    public boolean damageIfPossible(BaseEnemy self, Game.State state) {
        if (state.tick < this.dealDamageAfterTick) return false;
        if (this.playerPosCache.subtract(self.pos).magnitude() > this.intelligence.attackRange) return false;
        state.game.render.player.takeDamage(this.intelligence.attackDamage);
        this.dealDamageAfterTick = state.tick + this.intelligence.attackCooldownTicks;
        return true;
    }

    /**
     * Sets the animation of the enemy to have consistent visuals
     *
     * @param self      the enemy the AI-instance belongs to
     * @param direction the direction the enemy moved
     */
    public static void setAnimation(BaseEnemy self, Vector2 direction) {
        if (self.anim == null) return;
        if ((int) direction.x > 0) {
            self.anim.setAnim(2);
        } else if ((int) direction.x < 0) {
            self.anim.setAnim(1);
        } else if ((int) direction.y > 0) {
            self.anim.setAnim(0);
        } else if ((int) direction.y < 0) {
            self.anim.setAnim(3);
        }
    }
}
