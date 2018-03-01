package org.peon;

/**
 * Exception that should be thrown by the peon master when it is asked to execute a task that is blocked from executing
 * by another currently running task with the right (or wrong?) combinations of @ApplicationBlocking, @CategoryBlocking
 * and/or @ClassBlocking annotations present on the two task classes.
 *
 * It is recommended that applications use the getBlockingTask() methods in the peon master to check that the task is
 * allowed to be executed before executing it rather than catching this exception. This exception should indicate incorrect
 * behavior in the application.
 */
public class PeonTaskConcurrencyException extends RuntimeException {

    private static final long serialVersionUID = 8645999466554121417L;
    /**
     * The task that is blocking the other task from running.
     */
    private PeonTask blockingTask;
    /**
     * The task that is being blocked from running by the other task.
     */
    private PeonTask blockedTask;

    /**
     * Creates a new exception.
     *
     * @param blockingTask The task that is blocking the other task from running.
     * @param blockedTask The task that is being blocked from running by the other task.
     */
    public PeonTaskConcurrencyException(PeonTask blockingTask, PeonTask blockedTask) {
        super("The task " + blockedTask.getClass().getName() + " was blocked from starting by task " + blockingTask.getClass().getName() + ".");
        setBlockingTask(blockingTask);
        setBlockedTask(blockedTask);
    }

    /**
     * Gets the task that is blocking the other task from running.
     *
     * @return The task that is blocking the other task from running.
     */
    public PeonTask getBlockingTask() {
        return blockingTask;
    }
    /**
     * Sets the task that is blocking the other task from running.
     *
     * @param blockingTask The task that is blocking the other task from running.
     */
    public void setBlockingTask(PeonTask blockingTask) {
        this.blockingTask = blockingTask;
    }

    /**
     * Gets the task that is being blocked from running by the other task.
     *
     * @return The task that is being blocked from running by the other task.
     */
    public PeonTask getBlockedTask() {
        return blockedTask;
    }
    /**
     * Sets the task that is being blocked from running by the other task.
     *
     * @param blockedTask The task that is being blocked from running by the other task.
     */
    public void setBlockedTask(PeonTask blockedTask) {
        this.blockedTask = blockedTask;
    }

}
