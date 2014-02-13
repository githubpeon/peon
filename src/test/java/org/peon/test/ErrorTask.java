package org.peon.test;

import org.peon.AbstractPeonTask;

public class ErrorTask extends AbstractPeonTask<Thread> {

    public ErrorTask() {
        setName("Error Task");
        setDescription("A task that is destined for failure.");
    }

    @Override
    public void doExecute() {
        setResult(Thread.currentThread());
        error("Message", "Details.");
    }
}
