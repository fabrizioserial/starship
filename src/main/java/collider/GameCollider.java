package collider;

import edu.austral.dissis.starships.collision.Collider;
import strategy.BulletElement;

public interface GameCollider extends Collider<GameCollider> {

    default void handleCollisionWith(StartshipCollider ship) {}

    default void handleCollisionWith(BulletCollider bullet) {}

    default void handleCollisionWith(AsteroidCollider asteroid) {}

}
