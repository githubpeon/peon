package org.peon.event;

import org.peon.PeonTask;

/**
 * Event fired on the GUI event thread by the peon master when events occur in the task on the worker thread.
 */
public class PeonTaskEvent {

    public enum EventId {
        /**
         * The task is about to start. The task has been added to the current tasks in the peon master and task
         * starting objects that want to check for blocks for the tasks they start should perform such a check
         * when this event is fired.
         */
        STARTING,
        /**
         * The task has been started and is executing.
         */
        STARTED,
        /**
         * The task progressed. Either the progress property or the status property of the task has been updated.
         */
        PROGRESSED,
        /**
         * The status property of the task has been updated.
         */
        STATUS,
        /**
         * The task has been cancelled by user interaction.
         */
        CANCELLED,
        /**
         * The task has failed. An error object with a message for the user about why the task failed should be present in the task
         * when this event is fired.
         */
        FAILED,
        /**
         * An unhandled exception has been thrown while the task was executing. An exception object should be available in the task
         * when this event is fired.
         */
        EXCEPTION,
        /**
         * The task has finished successfully. If the task produces a result, the result should be present in the task when this event is fired.
         */
        FINISHED,
        /**
         * The end of the task lifecycle. The task has been removed from the current tasks in the peon master and task starting
         * objects that want to check for blocks for the tasks they start should perform such a check when this event is fired.
         */
        DONE;
    }

    /**
     * The task that triggered the event.
     */
    private PeonTask source;
    /**
     * Event id for the event.
     */
    private EventId id;

    /**
     * Creates an event.
     *
     * @param source The task that triggered the event.
     * @param id Event id for the event.
     */
    public PeonTaskEvent(PeonTask source, EventId id) {
        setSource(source);
        setId(id);
    }

    /**
     * Gets the task that triggered the event.
     *
     * @return The task that triggered the event.
     */
    public PeonTask getSource() {
        return source;
    }
    /**
     * Sets the task that triggered the event.
     *
     * @param source The task that triggered the event.
     */
    public void setSource(PeonTask source) {
        this.source = source;
    }

    /**
     * Gets the event id for the event.
     *
     * @return Event id for the event.
     */
    public EventId getId() {
        return id;
    }
    /**
     * Sets the event id for the event.
     *
     * @param id Event id for the event.
     */
    public void setId(EventId id) {
        this.id = id;
    }

}
