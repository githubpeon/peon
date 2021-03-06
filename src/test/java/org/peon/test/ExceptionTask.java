package org.peon.test;

import org.peon.AbstractPeonTask;

public class ExceptionTask extends AbstractPeonTask<Thread> {

    public ExceptionTask() {
        setName("Exception Task");
        setDescription("A task that will meet a spectacular end.");
    }
    
    @Override
    @SuppressWarnings("null")
    public void doExecute() {
        setResult(Thread.currentThread());
        String nullString = null;
        nullString.toString();
    }
}
