package com.github.githubpeon.peon.swing;

import java.beans.PropertyChangeEvent;

import javax.swing.SwingWorker;

import com.github.githubpeon.peon.AbstractPeonMaster;
import com.github.githubpeon.peon.Peon;
import com.github.githubpeon.peon.PeonTask;
import com.github.githubpeon.peon.event.PeonTaskEvent;
import com.github.githubpeon.peon.event.PeonTaskEvent.EventId;

/**
 * The PeonMaster keeps track of the active peons and tasks and relay events from the
 * running tasks to listeners on the GUI event thread. All tasks should be run by calling
 * the execute() method in a PeonMaster.
 *
 * This class is not threadsafe and its methods should only be called from the GUI event thread.
 *
 * It is recommended to use a singleton peon master in the application as each peon master
 * can only check for blocking tasks against the tasks it runs itself.
 *
 * This class provides an Swing implementation of the PeonMaster interface that should work for most situations
 * and that fires STARTED and DONE events when the SwingWorker peon state property changes to STARTED or DONE.
 */
public class SwingPeonMaster extends AbstractPeonMaster {

    /**
     * Swing peon masters create Swing peons to execute the tasks.
     *
     * @return A swing peon to execute the provided task.
     */
    @Override
    protected Peon createPeon(PeonTask peonTask) {
        return new SwingPeon(peonTask);
    }

    /**
     * Handles changes to the state property of the SwingWorker peon and relays them as STARTED and
     * DONE peon events.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        super.propertyChange(e);

        Peon peon = (Peon)e.getSource();
        PeonTask peonTask = peon.getPeonTask();

        if(e.getPropertyName().equals("state")) {
            if(e.getNewValue() == SwingWorker.StateValue.STARTED) {
                firePeonTaskEvent(new PeonTaskEvent(peonTask, EventId.STARTED));
            } else if(e.getNewValue() == SwingWorker.StateValue.DONE) {
                removePeonTask(peonTask);
                firePeonTaskEvent(new PeonTaskEvent(peonTask, EventId.DONE));
            }
        }
    }

}
