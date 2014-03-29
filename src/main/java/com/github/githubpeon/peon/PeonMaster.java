package com.github.githubpeon.peon;

import java.util.Set;

import com.github.githubpeon.peon.event.PeonTaskListener;

/**
 * The PeonMaster keeps track of the active peons and tasks and relay events from the
 * running tasks to listeners on the GUI event thread. All tasks should be run by calling
 * the execute() method in a PeonMaster.
 *
 * This class is not threadsafe and its methods should only be called from the GUI event thread.
 *
 * It is recommended to use a singleton peon master in the application as each peon master
 * can only check for blocking tasks against the tasks it runs itself.
 */
public interface PeonMaster {

    /**
     * Executes a task in a background worker thread.
     *
     * @param peonTask The task that will be executed.
     * @throws PeonTaskConcurrencyException if the task is blocked from running by another currenly running task.
     */
    public void execute(PeonTask peonTask);

    /**
     * Returns the set of all currently running tasks in this PeonMaster.
     *
     * @return A set of all currently running tasks in this PeonMaster.
     */
    public Set<PeonTask> getCurrentTasks();

    /**
     * Checks the class for @ApplicationBlocking, @CategoryBlocking or @ClassBlocking annotations and checks the set
     * of currently running tasks to see if any of them will prevent a task of the given class from running, depending
     * on which if any of those annotations are present on the class.
     *
     * @param clazz Will a task of this class be allowed to run or will it be blocked?
     * @return The currently running task that blocks a task of the given class from running or null if there is no such task.
     */
    public PeonTask getBlockingTaskFor(Class<? extends PeonTask> clazz);

    /**
     * Checks the object for a @StartsTasks annotation and performs a getBlockingTaskFor(Class) for all the classes
     * in the tasks value of the annotation if it is present.
     *
     * @param object Will a task started by this object be allowed to run or will it be blocked?
     * @return The currently running task that blocks a task of the given class from running or null if there is no such task.
     */
    public PeonTask getBlockingTaskFor(Object object);

    /**
     * Adds a tasklistener. Events from the running tasks will be dispatched to these listeners on
     * the GUI event thread.
     *
     * @param peonTaskListener The listener that will be added.
     */
    public void addPeonTaskListener(PeonTaskListener peonTaskListener);

    /**
     * Removes a tasklistener.
     *
     * @param peonTaskListener The listener that will be removed.
     */
    public void removePeonTaskListener(PeonTaskListener peonTaskListener);

}
