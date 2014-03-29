package com.github.githubpeon.peon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.githubpeon.peon.PeonTask;

/**
 * This annotation can be used in any class that somehow starts a task, for example a Swing Action to indicate
 * which class(es) of task if starts. With this annotation present the object can be passed to the getBlockingTask(Object)
 * method in the peon master to see if it is currently allowed to start the task or if the task class is currently
 * blocked from running by other currently running tasks.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface StartsTasks {
    Class<? extends PeonTask>[] tasks();
}
