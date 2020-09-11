package core;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The physics engine
 */
public class PhysicsEngine {

    private final Set<CollisionPair> currentCollision = new CopyOnWriteArraySet<>();

    /**
     * Check if any two objects are collided with each other
     *
     * @param gameObjects the list of objects to check collision
     */
    public void checkCollisions(List<GameObject> gameObjects) {
        checkAndHandleCollisionEntry(gameObjects);
        checkAndHandleCollisionExit();
    }

    private boolean within(Vector2 p, GameObject g) {
        return p.getX() >= g.getTransform().getPosition().getX()
                && p.getX() < g.getTransform().getPosition().getX() + g.getTransform().getSize().getX()
                && p.getY() >= g.getTransform().getPosition().getY()
                && p.getY() < g.getTransform().getPosition().getY() + g.getTransform().getSize().getY();
    }

    private boolean colliding(GameObject g1, GameObject g2) {
        Vector2 ll = g1.getTransform().getPosition();
        Vector2 lr = new Vector2(g1.getTransform().getPosition().getX() + g1.getTransform().getSize().getX() - 1,
                g1.getTransform().getPosition().getY());
        Vector2 ul = new Vector2(g1.getTransform().getPosition().getX(),
                g1.getTransform().getPosition().getY() + g1.getTransform().getSize().getY() - 1);
        Vector2 ur = new Vector2(g1.getTransform().getPosition().getX() + g1.getTransform().getSize().getX() - 1,
                g1.getTransform().getPosition().getY() + g1.getTransform().getSize().getY() - 1);

        return within(ll, g2) || within(lr, g2) || within(ul, g2) || within(ur, g2);
    }

    private void checkAndHandleCollisionEntry(List<GameObject> gameObjects) {
        // Compare each pair of game objects to see if they intersect. If they do,
        // call the onCollisionEnter method on both game objects. Note we call
        // onCollisionEnter only the first time the game objects intersect, and
        // will not call it again unless they have ceased to intersect before the
        // next intersection.

        // Our algorithm is very inefficient, doing a pairwise comparison over all
        // game objects. We trigger the collision on the first of the game objects.
        // We compare all pairs twice, so we trigger the collision on both objects.
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject g1 = gameObjects.get(i);
            for (int j = i + 1; j < gameObjects.size(); j++) {
                GameObject g2 = gameObjects.get(j);
                CollisionPair cp = new CollisionPair(g1, g2);
                if (g1 != g2 && colliding(g1, g2) && !currentCollision.contains(cp)) {
                    g1.onCollisionEnter(g2);
                    g2.onCollisionEnter(g1);
                    currentCollision.add(cp);
                }
            }
        }
    }

    private void checkAndHandleCollisionExit() {
        // Check each current pair of collisions, and if they are no longer colliding,
        // trigger their collision exit event and remove the collision from the
        // current list.
        List<CollisionPair> noLongerColliding = new ArrayList<>();
        for (CollisionPair cp : currentCollision) {
            if (!colliding(cp.g1, cp.g2)) {
                noLongerColliding.add(cp);
                cp.g2.onCollisionExit(cp.g1);
                cp.g1.onCollisionExit(cp.g2);
            }
        }
        for (CollisionPair cp : noLongerColliding) {
            currentCollision.remove(cp);
        }
    }

    private static class CollisionPair {
        private final GameObject g1;
        private final GameObject g2;

        private CollisionPair(GameObject g1, GameObject g2) {
            this.g1 = g1;
            this.g2 = g2;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (!(other instanceof CollisionPair)) return false;
            CollisionPair cp = (CollisionPair) other;
            return g1 == cp.g1 && g2 == cp.g2;
        }
    }
}
