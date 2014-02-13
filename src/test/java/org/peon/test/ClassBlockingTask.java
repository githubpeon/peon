package org.peon.test;

import org.peon.annotation.Category;
import org.peon.annotation.ClassBlocking;

@ClassBlocking
@Category("Test")
public class ClassBlockingTask extends EndlessTask {

    public ClassBlockingTask() {
        setName("Class Blocking Task");
        setDescription("A somewhat shy task that nonetheless prevents tasks of a similar class to run.");
    }
    
}
