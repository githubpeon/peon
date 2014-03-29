package com.github.githubpeon.peon;

import java.beans.PropertyChangeListener;

/**
 * A task that can be run in a background worker thread by a peon.
 *
 * @param <T> The type of the result that will be provided by the task.
 */
public interface PeonTask<T extends Object> {

    /**
     * A name that can be shown in the user interface, for example in a  table that lists all the currently running tasks.
     *
     * @return The name of the task.
     */
    public String getName();

    /**
     * A description that can be shown in the user interface of what the task does in general or what it is currently doing
     * if it is a more complicated task.
     *
     * @return A description of the task.
     */
    public String getDescription();

    /**
     * The total number of operations the task will perform. If, for example, the purpose of a task is to copy files
     * from one directory to another the total should be the number of files in the source directory. If the total is
     * unknown this method should return -1.
     *
     * @return The total number of operations for the task or -1 if the total number is unknown.
     */
    public int getTotal();

    /**
     * The number of operations that have been performed by the task so far.
     *
     * @return The number of operations that have been performed by the task so far.
     */
    public int getProgress();

    /**
     * Called by the peon in a worker thread. The actual work that is to be done by the task should be done in (or called by)
     * this method.
     */
    public void execute();

    /**
     * Cancels the task and terminates the worker thread.
     */
    public void cancel();

    /**
     * Gets the result of the task if the task produces a result. This should be the object(s) the rest of the
     * application is interested in when it runs the task.
     *
     * @return The result of the task or null if the task does not produce a result.
     */
    public T getResult();

    /**
     * When a not unexpected error occurs while the task is running the state of the task should be
     * set to error and this method should return an error object with messages that can be presented
     * to the user.
     *
     * @return An error if the state of the task is ERROR and null if it is not.
     */
    public PeonError getError();

    /**
     * When an unexpected error occurs while the task is running this method should provide the
     * exception that was thrown.
     *
     * @return Exception that was thrown while the task was running or null if no exception has been thrown.
     */
    public Exception getException();

    /**
     * The current state of the task.
     *
     * @return The current state of the task.
     */
    public PeonTaskState getState();

    /**
     * The current status message of the task. This message is intended to be displayed to the user
     * to inform the user of what the task is currently doing.
     *
     * @return A message describing the current status of the task.
     */
    public String getStatus();

    /**
     * The System.currentTimeMillis() the task started executing.
     *
     * @return The time in milliseconds the task started executing.
     */
    public long getStartTime();

    /**
     * The System.currentTimeMillis() the task stopped executing or -1 of it has not yet stopped.
     *
     * @return The time in milliseconds the task stopped executing or -1 of it has not yet stopped.
     */
    public long getEndTime();

    /**
     * The time in milliseconds the task has been executing.
     *
     * @return The time in milliseconds the task has been executing.
     */
    public long getTimeElapsed();

    /**
     * The time in milliseconds the task is estimated to keep running or -1 if the time can't be estimated.
     *
     * @return The time in milliseconds the task is estimated to keep running or -1 if the time can't be estimated.
     */
    public long getEstimatedTimeRemaining();

    /**
     * Adds a property change listener to the task. Only peons that run the task are intended to be added here.
     * Be aware, if non peon listeners are added, that events dispatched to these listeners are fired
     * on the worker thread and not the GUI event thread.
     *
     * @param propertyChangeListener The listener that will be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    /**
     * Removes a property change listener from the task.
     *
     * @param propertyChangeListener The listener that will be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

}
