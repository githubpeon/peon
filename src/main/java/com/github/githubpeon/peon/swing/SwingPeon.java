package com.github.githubpeon.peon.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.SwingWorker;

import com.github.githubpeon.peon.Peon;
import com.github.githubpeon.peon.PeonTask;
import com.github.githubpeon.peon.PeonTaskState;

/**
 * The peon is the object that makes sure the peon tasks are run in a separate worker thread and that
 * relays events from the task on the worker thread to the peon master on the GUI event thread. The peon master
 * creates a new peon for each task the master is told to execute and then tells that peon to work().
 *
 * This is an implementation of a peon for a Swing application. It extends SwingWorker for convenient
 * communication between a worker thread and the Swing Event Dispatch Thread.
 */
public class SwingPeon extends SwingWorker<PeonTask, PropertyChangeEvent> implements Peon, PropertyChangeListener {

    private PeonTask peonTask;

    public SwingPeon(PeonTask peonTask) {
        setPeonTask(peonTask);
    }

    /**
     * The task the peon will execute.
     *
     * @return The task the peon will execute.
     */
    @Override
    public PeonTask getPeonTask() {
        return peonTask;
    }
    /**
     * Sets the task the peon will execute and adds itself as a property listener on the task.
     *
     * @param peonTask The task the peon will execute.
     */
    @Override
    public void setPeonTask(PeonTask peonTask) {
        this.peonTask = peonTask;
        peonTask.addPropertyChangeListener(this);
    }

    /**
     * Tells the peon to start working on its task in a separate worker thread. It calls execute() to start
     * the SwingWorker in a separate worker thread.
     */
    @Override
    public void work() {
        execute();
    }

    /**
     * This is the SwingWorker method that is run in the worker thread when the SwingWorker is executed.
     * It calls execute() on the task and returns the task itself as a result when the task has stopped.
     *
     * @return peonTask The task that was executed by the peon.
     */
    @Override
    public PeonTask doInBackground() {
        peonTask.execute();
        return peonTask;
    }

    /**
     * Processes events fired by the task on the worker thread by relaying them to the peons own
     * property change listeners on the Swing Event Dispatch Thread.
     *
     * @param events List of events fired by the task on the worker thread.
     */
    @Override
    protected void process(List<PropertyChangeEvent> events) {
        for(PropertyChangeEvent event : events) {
            firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
        }
    }

    /**
     * Handles an event fired by the task on the worker thread by publish()ing it on the Swing Event Dispatch Thread.
     * If the property is the task state and the new value is PeonTaskState.CANCELLED or PeonTaskState.FAILED, it
     * will also call cancel(true) on itself to terminate the worker thread.
     *
     * @param e The event that will be relayed from the task on the worker thread to the property change listeners
     *          on the Event Dispatch Thread.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        publish(e);
        if(e.getPropertyName().equals("state")) {
            if(e.getNewValue() == PeonTaskState.CANCELLED
               || e.getNewValue() == PeonTaskState.FAILED) {
                cancel(true);
            }
        }
    }

}
