package org.peon.test;

import org.peon.AbstractPeonTask;
import org.peon.annotation.Category;

@Category("Test")
public class SuccessTask extends AbstractPeonTask<Thread> {

    public SuccessTask() {
        setName("Success Task");
        setDescription("Just peeking into another thread for a very short while.");
    }

    @Override
    public void doExecute() {
        setResult(Thread.currentThread());
    }
}
