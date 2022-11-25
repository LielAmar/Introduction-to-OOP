//package src.brick_strategies;
//
//import danogl.GameObject;
//import danogl.collisions.GameObjectCollection;
//import danogl.gui.Sound;
//import danogl.gui.rendering.Renderable;
//import danogl.util.Counter;
//import danogl.util.Vector2;
//import src.gameobjects.Puck;
//
//public class DoublePowerupStrategy extends CollisionStrategy {
//
//    private final StrategyRandomizer strategyRandomizer;
//
//    public DoublePowerupStrategy(GameObjectCollection gameObjects, StrategyRandomizer strategyRandomizer) {
//        super(gameObjects);
//
//        this.strategyRandomizer = strategyRandomizer;
//    }
//
//    @Override
//    public void onCollision(GameObject collidedObject, GameObject colliderObject, Counter bricksCounter) {
//        super.onCollision(collidedObject, colliderObject, bricksCounter);
//
//        // Calculating the size of a puck depending on the size of the
//        float puckDiameter = collidedObject.getDimensions().x() / 3;
//
//        for(int i = 0; i < PUCKS_TO_ADD; i++) {
//            GameObject puck = new Puck(
//                    collidedObject.getTopLeftCorner(),
//                    new Vector2(puckDiameter, puckDiameter),
//                    renderable,
//                    sound);
//
//            puck.setCenter(new Vector2(
//                    collidedObject.getTopLeftCorner().x() + (i * puckDiameter) + (puckDiameter / 2),
//                    collidedObject.getCenter().y()
//            ));
//
//            super.gameObjects.addGameObject(puck);
//        }
//    }
//}
