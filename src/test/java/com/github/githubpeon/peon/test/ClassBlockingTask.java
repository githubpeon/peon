package com.github.githubpeon.peon.test;

import com.github.githubpeon.peon.annotation.Category;
import com.github.githubpeon.peon.annotation.ClassBlocking;

@ClassBlocking
@Category("Test")
public class ClassBlockingTask extends EndlessTask {

    public ClassBlockingTask() {
        setName("Class Blocking Task");
        setDescription("A somewhat shy task that nonetheless prevents tasks of a similar class to run.");
    }
    
}
