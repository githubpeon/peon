package org.peon.test;

import org.peon.AbstractPeonTask;

public class EndlessTask extends AbstractPeonTask<Thread> {

    public EndlessTask() {
        setName("Endless Task");
        setDescription("A task that never seems to get where it's going.");
    }

    @Override
    public void doExecute() {
        while(true);
    }
}
