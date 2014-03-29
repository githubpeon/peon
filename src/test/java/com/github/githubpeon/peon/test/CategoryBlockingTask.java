package com.github.githubpeon.peon.test;

import com.github.githubpeon.peon.annotation.Category;
import com.github.githubpeon.peon.annotation.CategoryBlocking;

@CategoryBlocking
@Category("Test")
public class CategoryBlockingTask extends EndlessTask {

    public CategoryBlockingTask() {
        setName("Category Blocking Task");
        setDescription("An bully task that won't allow any at all similar tasks to run at the same time.");
    }

}
