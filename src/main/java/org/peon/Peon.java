package org.peon;

import java.beans.PropertyChangeListener;

/**
 * The peon is the object that makes sure the peon tasks are run in a separate worker thread and that
 * relays events from the task on the worker thread to the peon master on the GUI event thread. The peon master
 * creates a new peon for each task the master is told to execute and then tells that peon to work().
 */
public interface Peon {

    /**
     * Tells the peon to start working on its task in a separate worker thread.
     */
    public void work();

    /**
     * The task the peon will execute.
     *
     * @return The task the peon will execute.
     */
    public PeonTask getPeonTask();

    /**
     * Sets the task the peon will execute.
     *
     * @param peonTask The task the peon will execute.
     */
    public void setPeonTask(PeonTask peonTask);

    /**
     * Adds a property change listener to the peon. These listeners will receive events fired by
     * the task on the worker thread in the GUI event thread.
     *
     * @param propertyChangeListener The listener that will be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    /**
     * Removes a property change listener from the peon.
     *
     * @param propertyChangeListener The listener that will be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

}
