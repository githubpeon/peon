package com.github.githubpeon.peon;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A task that can be run in a background worker thread by a peon.
 *
 * This class provides an implementation of the PeonTask interface that should work for most situations. Subclasses
 * should implement the doExecute() method to perform the actual work.
 *
 * @param <T> The type of the result that will be provided by the task.
 */
public abstract class AbstractPeonTask<T extends Object> implements PeonTask<T> {

    /**
     * Keeps track of and sends events to registered property change listeners.
     */
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    /**
     * A name that can be shown in the user interface, for example in a  table that lists all the currently running tasks.
     */
    private String name;
    /**
     * A description that can be shown in the user interface of what the task does in general or what it is currently doing
     * if it is a more complicated task.
     */
    private String description;
    /**
     * The total number of operations for the task or -1 if the total number is unknown.
     */
    private int total = -1;
    /**
     * The number of operations that have been performed by the task so far.
     */
    private int progress = 0;
    /**
     * The result of the task or null if the task does not produce a result.
     */
    private T result;
    /**
     * An error if the state of the task is ERROR and null if it is not.
     */
    private PeonError error;
    /**
     * Exception that was thrown while the task was running or null if no exception has been thrown.
     */
    private Exception exception;
    /**
     * The current state of the task.
     */
    private PeonTaskState state = PeonTaskState.PENDING;
    /**
     * A message describing the current status of the task.
     */
    private String status;

    /**
     * The System.currentTimeMillis() the task started executing,
     */
    private long startTime = -1;

    /**
     * The System.currentTimeMillis() the task stopped executing or -1 of it has not yet stopped.
     */
    private long endTime = -1;

    /**
     * A name that can be shown in the user interface, for example in a  table that lists all the currently running tasks.
     *
     * @return The name of the task.
     */
    @Override
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the task. This is a bound property.
     *
     * @param name The name of the task.
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", oldName, this.name);
    }

    /**
     * A description that can be shown in the user interface of what the task does in general or what it is currently doing
     * if it is a more complicated task.
     *
     * @return A description of the task.
     */
    @Override
    public String getDescription() {
        return description;
    }
    /**
     * Sets the description of the task. This is a bound property.
     *
     * @param description The description of the task.
     */
    public void setDescription(String description) {
        String oldDescription = this.description;
        this.description = description;
        propertyChangeSupport.firePropertyChange("description", oldDescription, this.description);
    }

    /**
     * The total number of operations the task will perform. If, for example, the purpose of a task is to copy files
     * from one directory to another the total should be the number of files in the source directory. If the total is
     * unknown this method should return -1.
     *
     * @return The total number of operations for the task or -1 if the total number is unknown.
     */
    @Override
    public int getTotal() {
        return total;
    }
    /**
     * Sets the total number of operations the task will perform. This is a bound property.
     *
     * @param total The total number of operations the task will perform.
     */
    protected void setTotal(int total) {
        int oldTotal = this.total;
        this.total = total;
        propertyChangeSupport.firePropertyChange("total", oldTotal, this.total);
    }

    /**
     * The number of operations that have been performed by the task so far.
     *
     * @return The number of operations that have been performed by the task so far.
     */
    @Override
    public int getProgress() {
        return progress;
    }
    /**
     * Sets the total number of operations that have been performed by the task so far. This is a bound property.
     * It is recommended to use the progress() and progress(String) methods instead of using this method directly
     * since those methods check the current state of the task before progressing.
     *
     * @param progress
     */
    protected void setProgress(int progress) {
        int oldProgress = this.progress;
        this.progress = progress;
        propertyChangeSupport.firePropertyChange("progress", oldProgress, this.progress);
    }

    /**
     * Gets the result of the task if the task produces a result. This should be the object(s) the rest of the
     * application is interested in when it runs the task.
     *
     * @return The result of the task or null if the task does not produce a result.
     */
    @Override
    public T getResult() {
        return result;
    }
    /**
     * Sets the result of the task. This is a bound property.
     *
     * @param result The result of the task.
     */
    protected void setResult(T result) {
        T oldResult = this.result;
        this.result = result;
        propertyChangeSupport.firePropertyChange("result", oldResult, this.result);
    }

    /**
     * When a not unexpected error occurs while the task is running the state of the task should be
     * set to error and this method should return an error object with messages that can be presented
     * to the user.
     *
     * @return An error if the state of the task is ERROR and null if it is not.
     */
    @Override
    public PeonError getError() {
        return error;
    }
    /**
     * Sets an error that occurs when the task is running. This is a bound property It is recommended to use
     * the error(String, String) method instead of using this method directly since that method also checks
     * and sets the state of the task.
     *
     * @param error
     */
    protected void setError(PeonError error) {
        PeonError oldError = this.error;
        this.error = error;
        propertyChangeSupport.firePropertyChange("error", oldError, this.error);
    }

    /**
     * When an unexpected error occurs while the task is running this method should provide the
     * exception that was thrown.
     *
     * @return Exception that was thrown while the task was running or null if no exception has been thrown.
     */
    @Override
    public Exception getException() {
        return exception;
    }
    /**
     * Sets an exception that is thrown when the task is running. This is a bound property. It is not recommended
     * to use this or the exception(Exception) method directly since the implementation of execute() in this class already
     * deals with catching and handling of unhandled exceptions.
     *
     * @param error
     */
    protected void setException(Exception exception) {
        Exception oldException = this.exception;
        this.exception = exception;
        propertyChangeSupport.firePropertyChange("exception", oldException, this.exception);
    }

    /**
     * The current state of the task.
     *
     * @return The current state of the task.
     */
    @Override
    public PeonTaskState getState() {
        return state;
    }
    /**
     * Sets the current state of the task. This is a bound property.
     *
     * @param peonTaskState The current state of the task. It is recommended to use the activate(), cancel(), error()
     * exception() and finish() methods instead of using this method directly since those methods check state transfer
     * preconditions and this method does not.
     */
    protected void setState(PeonTaskState peonTaskState) {
        PeonTaskState oldState = this.state;
        this.state = peonTaskState;
        propertyChangeSupport.firePropertyChange("state", oldState, this.state);
    }

    /**
     * The current status message of the task. This message is intended to be displayed to the user
     * to inform the user of what the task is currently doing.
     *
     * @return A message describing the current status of the task.
     */
    @Override
    public String getStatus() {
        return status;
    }
    /**
     * Sets the current status message of the task. This is a bound property. It is recommended to use the progress(String)
     * method instead of using this method directly since that method also checks the state of the task.
     *
     * @param status The current status message of the task.
     */
    protected void setStatus(String status) {
        String oldStatus = this.status;
        this.status = status;
        propertyChangeSupport.firePropertyChange("status", oldStatus, this.status);
    }

    /**
     * The System.currentTimeMillis() the task started executing.
     *
     * @return The time in milliseconds the task started executing.
     */
    public long getStartTime() {
        return 0;
    }

    /**
     * The System.currentTimeMillis() the task stopped executing or -1 of it has not yet stopped.
     *
     * @return The time in milliseconds the task stopped executing or -1 of it has not yet stopped.
     */
    public long getEndTime() {
        return 0;
    }

    /**
     * The time in milliseconds the task has been executing.
     *
     * @return The time in milliseconds the task has been executing.
     */
    public long getTimeElapsed() {
        if(endTime == -1) {
            return System.currentTimeMillis() - this.startTime;
        } else {
            return this.endTime - this.startTime;
        }
    }

    /**
     * The time in milliseconds the task is estimated to keep running or -1 if the time can't be estimated.
     * The estimation is crude. It divides the elapsed time by the number of operations (=progress) have been
     * performed and multiplies the result with the number of operations left to do.
     *
     * If the total is unknown, this method will return -1.
     *
     * @return The time in milliseconds the task is estimated to keep running or -1 if the time can't be estimated.
     */
    public long getEstimatedTimeRemaining() {
        if(this.total == -1) {
            return -1;
        }
        if(getProgress() == 0) {
            return -1;
        }
        long timePerOperation = getTimeElapsed() / getProgress();
        return (getTotal() - getProgress()) * timePerOperation;
    }

    /**
     * Called by the peon in a worker thread. Calls, in order, activate(), doExecute(), exception() (if an
     * exception is thrown by doExecute()) and finish(). Subclasses should implement doExecute() to do the
     * actual work the task is supposed to do.
     */
    @Override
    public void execute() {
        this.startTime = System.currentTimeMillis();
        activate();
        try {
            doExecute();
        } catch(Exception e) {
            exception(e);
        }
        finish();
        this.endTime = System.currentTimeMillis();
    }

    /**
     * Implement this method to do the actual work the task is supposed to do.
     */
    protected abstract void doExecute();

    /**
     * Increments the progress property of the task by 1 if the current state of the task is PeonTaskState.ACTIVE.
     * The progress property indicates the number of units of work that have already been performed so this method
     * should be called after a unit has been performed, not before it is going to be performed.
     */
    protected void progress() {
        progress(this.status);
    }

    /**
     * Increments the progress property of the task by 1 and sets the state message of the task to the provided string
     * if the current state of the task is PeonTaskState.ACTIVE.
     * The progress property indicates the number of units of work that have already been performed so this method
     * should be called after a unit has been performed, not before it is going to be performed.     *
     *
     * @status The new status message of the task.
     */
    protected synchronized void progress(String status) {
        if(getState() == PeonTaskState.ACTIVE) {
            setProgress(this.progress + 1);
            setStatus(status);
        }
    }

    /**
     * Changes the state of the task to PeonTaskState.ACTIVE if the current state of the task is PeonTaskState.PENDING. Normally
     * this does not have to be done in subclasses since the execute() method already does this.
     */
    protected synchronized void activate() {
        if(getState() == PeonTaskState.PENDING) {
            setState(PeonTaskState.ACTIVE);
        }
    }

    /**
     * Cancels the task and terminates the worker thread if the current state of the task is PeonTaskState.ACTIVE. This method
     * is public since it's intended to be used to allow the rest of the application to cancel a task by user interaction.
     */
    public synchronized void cancel() {
        if(getState() == PeonTaskState.ACTIVE) {
            setState(PeonTaskState.CANCELLED);
        }
    }

    /**
     * This method should be used when an expected sort of error occurs while the task is running. If the current state of
     * the task is PeonTaskState.ACTIVE, this method creates an error and sets it and sets the state of the task to PeonTaskState.FAILED.
     *
     * @param message The error message of the error.
     * @param details The error details of the error.
     */
    protected synchronized void error(String message, String details) {
        if(getState() == PeonTaskState.ACTIVE) {
            setError(new PeonError(message, details));
            setState(PeonTaskState.FAILED);
        }
    }

    /**
     * Sets the exception and sets the state of the task to PeonTaskState.EXCEPTION if the current state of the task is
     * PeonTaskState.ACTIVE. Normally this does not have to be done in subclasses since the execute() method already does
     * this when it catches and unhandled exception.
     *
     * @param exception The exception that was thrown.
     */
    protected synchronized void exception(Exception exception) {
        if(getState() == PeonTaskState.ACTIVE) {
            setException(exception);
            setState(PeonTaskState.EXCEPTION);
        }
    }

    /**
     * Sets the state of the task to PeonTaskState.FINISHED if the current state of the task is PeonTaskState.ACTIVE. Normally this
     * does not have to be done in subclasses since the execute() method already does this.
     */
    protected synchronized void finish() {
        if(getState() == PeonTaskState.ACTIVE) {
            setState(PeonTaskState.FINISHED);
        }
    }

    /**
     * Adds a property change listener to the task. Only peons that run the task are intended to be added here.
     * Be aware, if non peon listeners are added, that events dispatched to these listeners are fired
     * on the worker thread and not the GUI event thread.
     *
     * @param propertyChangeListener The listener that will be added.
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    /**
     * Removes a property change listener from the task.
     *
     * @param propertyChangeListener The listener that will be removed.
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
    }

}
