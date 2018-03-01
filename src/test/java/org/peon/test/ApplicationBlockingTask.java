package org.peon.test;

import org.peon.annotation.ApplicationBlocking;
import org.peon.annotation.Category;

@ApplicationBlocking
@Category("Test")
public class ApplicationBlockingTask extends EndlessTask {

    public ApplicationBlockingTask() {
        setName("Application Blocking Task");
        setDescription("An attention starved task that demands to be the only task the application cares about.");
    }

}
