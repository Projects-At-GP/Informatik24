package enemy.ai;

import vector.Vector2;

public interface BaseEnemyInterface {
    public void getAlerted(Vector2 targetPos);
    public Vector2 getPos();
    public EnemyAI getAI();
}
