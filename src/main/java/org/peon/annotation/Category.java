package org.peon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates which (if any) category of tasks the task belongs to. This category is used to decide
 * if the task blocks or is blocked by any task when rules for @CategoryBlocking tasks are evaluated.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Category {
    /**
     * The name of the category the task belongs to.
     *
     * @return The name of the category the task belongs to.
     */
    String value();
}
