package org.peon.test;

import org.peon.AbstractPeonTask;

public class CancelTask extends AbstractPeonTask<Thread> {

    public CancelTask() {
        setName("Cancel Task");
        setDescription("A task that will never be able to get to the finish line.");
    }

    @Override
    public void doExecute() {
        setResult(Thread.currentThread());
        cancel();
    }
}
