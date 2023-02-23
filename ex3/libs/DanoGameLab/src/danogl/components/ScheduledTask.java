package danogl.components;

import danogl.GameObject;

/**
 * A component for scheduling a task for execution at a later time
 * @author Dan Nirel
 */
public class ScheduledTask implements Component {
    private final GameObject gameObjectToUpdateThrough;
    private final float waitTime;
    private final boolean repeat;
    private final Runnable onElapsed;

    private double timePassed = 0;

    /**
     * Create a scheduled task. No other calls to the object are necessary.
     * @param gameObjectToUpdateThrough A GameObject related to the task.
     *                                  It is not actually important what GameObject is supplied here.
     *                                  The task will become a Component of that object,
     *                                  as a means of calling this task's update method.
     *                                  If and when possible, the task will remove itself
     *                                  from the object's components.
     * @param waitTime The delay until the task is executed, in seconds.
     *                 The actual time might, of course, be a little bit longer,
     *                 depending on the frequency at which updates to the game are made (the FPS).
     * @param repeat Should the task be executed once, or every waitTime?
     * @param onElapsed The task to execute.
     */
    public ScheduledTask(GameObject gameObjectToUpdateThrough, float waitTime,
                         boolean repeat, Runnable onElapsed) {
        if(gameObjectToUpdateThrough == null || onElapsed == null)
            throw new NullPointerException("Invalid parameters for ScheduledTask");
        this.gameObjectToUpdateThrough = gameObjectToUpdateThrough;
        this.waitTime = waitTime;
        this.repeat = repeat;
        this.onElapsed = onElapsed;
        gameObjectToUpdateThrough.addComponent(this);
    }

    /**
     * Reset the clock, make the task start counting time from now instead of since
     * the call to the constructor or the last call to reset.
     */
    public void reset() {
        timePassed = 0;
    }

    @Override
    public void update(float deltaTime) {
        timePassed += deltaTime;
        if(timePassed >= waitTime) {
            if(onElapsed != null)
                onElapsed.run();
            if(repeat)
                timePassed = 0;
            else
                gameObjectToUpdateThrough.removeComponent(this);
        }
    }
}
