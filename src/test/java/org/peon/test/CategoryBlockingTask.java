package org.peon.test;

import org.peon.annotation.Category;
import org.peon.annotation.CategoryBlocking;

@CategoryBlocking
@Category("Test")
public class CategoryBlockingTask extends EndlessTask {

    public CategoryBlockingTask() {
        setName("Category Blocking Task");
        setDescription("An bully task that won't allow any at all similar tasks to run at the same time.");
    }

}
