package org.peon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tasks with this annotation present will be blocked from running by any other currently running tasks
 * with the same @Category in the peon master and will block all other such tasks from running while it is running itself.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CategoryBlocking {

}
