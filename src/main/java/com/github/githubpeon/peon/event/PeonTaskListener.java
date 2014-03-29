package com.github.githubpeon.peon.event;

import java.util.EventListener;

/**
 * A listener that will be notified of events triggered by executing tasks. The listeners will
 * be notified of these events on the GUI event thread if they register as listeners with the peon master.
 */
public interface PeonTaskListener extends EventListener {

    /**
     * Receives events fired by the tasks relayed by the peon master.
     *
     * @param e The event that was fired.
     */
    public void peonTaskEvent(PeonTaskEvent e);

}
