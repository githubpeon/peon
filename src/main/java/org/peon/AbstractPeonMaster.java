package org.peon;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import org.peon.annotation.ApplicationBlocking;
import org.peon.annotation.Category;
import org.peon.annotation.CategoryBlocking;
import org.peon.annotation.ClassBlocking;
import org.peon.annotation.StartsTasks;
import org.peon.event.PeonTaskEvent;
import org.peon.event.PeonTaskListener;
import org.peon.event.PeonTaskEvent.EventId;

/**
 * The PeonMaster keeps track of the active peons and tasks and relay events from the
 * running tasks to listeners on the GUI event thread. All tasks should be run by calling
 * the execute() method in a PeonMaster.
 *
 * This class is not threadsafe and its methods should only be called from the GUI event thread.
 *
 * It is recommended to use a singleton peon master in the application as each peon master
 * can only check for blocking tasks against the tasks it runs itself.
 *
 * This class provides an implementation of the PeonMaster interface that should work for most situations.
 */
public abstract class AbstractPeonMaster implements PeonMaster, PropertyChangeListener {

    /**
     * The currently active tasks for this PeonMaster.
     */
    private final Set<PeonTask> currentTasks = new HashSet<PeonTask>();
    /**
     * Task listeners for this PeonMaster.
     */
    private final Set<PeonTaskListener> peonTaskListeners = new HashSet<PeonTaskListener>();

    /**
     * Executes a task in a background worker thread.
     *
     * @param peonTask The task that will be executed.
     * @throws PeonTaskConcurrencyException if the task is blocked from running by another currenly running task.
     */
    @Override
    public void execute(PeonTask peonTask) {
        PeonTask blockingTask = getBlockingTaskFor(peonTask.getClass());
        if(blockingTask != null) {
            throw new PeonTaskConcurrencyException(blockingTask, peonTask);
        }
        addPeonTask(peonTask);
        firePeonTaskEvent(new PeonTaskEvent(peonTask, EventId.STARTING));
        Peon peon = createPeon(peonTask);
        peon.addPropertyChangeListener(this);
        peon.work();
    }

    /**
     * Override this method in subclasses to provide a Peon that can be used to execute the provided
     * task in the GUI environment used in the application.
     *
     * @param peonTask The task that will be executed by the Peon provided by this method.
     * @return A Peon that can execute the provided task.
     */
    protected abstract Peon createPeon(PeonTask peonTask);

    /**
     * Returns the set of all currently running tasks in this PeonMaster.
     *
     * @return A set of all currently running tasks in this PeonMaster.
     */
    @Override
    public Set<PeonTask> getCurrentTasks() {
        Set<PeonTask> currentTasks = new HashSet<PeonTask>();
        currentTasks.addAll(this.currentTasks);
        return currentTasks;
    }

    /**
     * Checks the class for @ApplicationBlocking, @CategoryBlocking or @ClassBlocking annotations and checks the set
     * of currently running tasks to see if any of them will prevent a task of the given class from running, depending
     * on which if any of those annotations are present on the class.
     *
     * @param clazz Will a task of this class be allowed to run or will it be blocked?
     * @return The currently running task that blocks a task of the given class from running or null if there is no such task.
     */
    @Override
    public PeonTask getBlockingTaskFor(Class<? extends PeonTask> clazz) {
        for(PeonTask currentTask : this.currentTasks) {
            Class<? extends PeonTask> currentClass = currentTask.getClass();
            if(clazz.isAnnotationPresent(ApplicationBlocking.class)
               || currentClass.isAnnotationPresent(ApplicationBlocking.class)) {
                return currentTask;
            } else if(clazz.isAnnotationPresent(CategoryBlocking.class)
                      || currentClass.isAnnotationPresent(CategoryBlocking.class)) {
                String category = (clazz.isAnnotationPresent(Category.class) ? clazz.getAnnotation(Category.class).value() : "");
                String currentCategory = (currentClass.isAnnotationPresent(Category.class) ? currentClass.getAnnotation(Category.class).value() : "");
                if(category.equals(currentCategory)) {
                    return currentTask;
                }
            } else if(clazz.isAnnotationPresent(ClassBlocking.class)
                      || currentClass.isAnnotationPresent(ClassBlocking.class)) {
                if(clazz.equals(currentClass)) {
                    return currentTask;
                }
            }
        }
        return null;
    }

    /**
     * Checks the object for a @StartsTasks annotation and performs a getBlockingTaskFor(Class) for all the classes
     * in the tasks value of the annotation if it is present.
     *
     * @param object Will a task started by this object be allowed to run or will it be blocked?
     * @return The currently running task that blocks a task of the given class from running or null if there is no such task.
     */
    @Override
    public PeonTask getBlockingTaskFor(Object object) {
        if(object.getClass().isAnnotationPresent(StartsTasks.class)) {
            Class<? extends PeonTask>[] classes = object.getClass().getAnnotation(StartsTasks.class).tasks();
            for(Class<? extends PeonTask> clazz : classes) {
                PeonTask blockingTask = getBlockingTaskFor(clazz);
                if(blockingTask != null) {
                    return blockingTask;
                }
            }
            return null;
        } else {
            throw new IllegalArgumentException("Class " + object.getClass().getName() + " is not annotated with @StartsTasks.");
        }
    }

    /**
     * Handles property change events from the tasks and/or peon and translates them
     * into suitable peon task events that will be dispatched to task listeners on
     * the GUI event thread.
     *
     * @param e A property change event from a currently active task and/or peon.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Peon peon = (Peon)e.getSource();
        PeonTask peonTask = peon.getPeonTask();

        if(e.getPropertyName().equals("state")) {
            if(e.getNewValue() == PeonTaskState.CANCELLED) {
                firePeonTaskEvent(new PeonTaskEvent(peonTask, EventId.CANCELLED));
            } else if(e.getNewValue() == PeonTaskState.FAILED) {
                firePeonTaskEvent(new PeonTaskEvent(peonTask, EventId.FAILED));
            } else if(e.getNewValue() == PeonTaskState.EXCEPTION) {
                firePeonTaskEvent(new PeonTaskEvent(peonTask, EventId.EXCEPTION));
            } else if(e.getNewValue() == PeonTaskState.FINISHED) {
                firePeonTaskEvent(new PeonTaskEvent(peonTask, EventId.FINISHED));
            }
        } else if(e.getPropertyName() == "progress") {
            firePeonTaskEvent(new PeonTaskEvent(peonTask, EventId.PROGRESSED));
        } else if(e.getPropertyName().equals("status")) {
          firePeonTaskEvent(new PeonTaskEvent(peonTask, EventId.STATUS));
        }
    }

    /**
     * Adds a task to the set of currently active tasks.
     *
     * @param peonTask The task that will be added to the set of currently active tasks.
     */
    protected void addPeonTask(PeonTask peonTask) {
        this.currentTasks.add(peonTask);
    }

    /**
     * Removes a task from the set of currently active tasks.
     *
     * @param peonTask The task that will be removed from the set of currently active tasks.
     */
    protected void removePeonTask(PeonTask peonTask) {
        this.currentTasks.remove(peonTask);
    }

    /**
     * Dispatches a peon task event to all registered listeners.
     *
     * @param e The event that will be dispatched.
     */
    protected void firePeonTaskEvent(PeonTaskEvent e) {
        for(PeonTaskListener peonLTaskistener : peonTaskListeners) {
            peonLTaskistener.peonTaskEvent(e);
        }
    }

    /**
     * Adds a tasklistener. Events from the running tasks will be dispatched to these listeners on
     * the GUI event thread.
     *
     * @param peonTaskListener The listener that will be added.
     */
    @Override
    public void addPeonTaskListener(PeonTaskListener peonTaskListener) {
        this.peonTaskListeners.add(peonTaskListener);
    }

    /**
     * Removes a tasklistener.
     *
     * @param peonTaskListener The listener that will be removed.
     */
    @Override
    public void removePeonTaskListener(PeonTaskListener peonTaskListener) {
        this.peonTaskListeners.remove(peonTaskListener);
    }

}
