package org.peon;

/**
 * The possible states of a peon task.
 */
public enum PeonTaskState {

    /**
     * The task has been created and is ready to start executing.
     */
    PENDING,
    /**
     * The task is currently executing.
     */
    ACTIVE,
    /**
     * The task has been cancelled by user interaction.
     */
    CANCELLED,
    /**
     * The task has failed to produce the desired result. A task in this state can be expected to have
     * an error with a message and details that can be presented to the user to show why the task failed.
     */
    FAILED,
    /**
     * An unhandled exception was thrown when the task was executing.
     */
    EXCEPTION,
    /**
     * The task finished successfully. If the task produces a result, the result should be available from
     * a task in this state.
     */
    FINISHED;

}
