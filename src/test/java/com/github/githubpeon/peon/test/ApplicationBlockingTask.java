package com.github.githubpeon.peon.test;

import com.github.githubpeon.peon.annotation.ApplicationBlocking;
import com.github.githubpeon.peon.annotation.Category;

@ApplicationBlocking
@Category("Test")
public class ApplicationBlockingTask extends EndlessTask {

    public ApplicationBlockingTask() {
        setName("Application Blocking Task");
        setDescription("An attention starved task that demands to be the only task the application cares about.");
    }

}
