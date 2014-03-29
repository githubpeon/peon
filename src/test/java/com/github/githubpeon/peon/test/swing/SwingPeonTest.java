package com.github.githubpeon.peon.test.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.githubpeon.peon.PeonMaster;
import com.github.githubpeon.peon.PeonTask;
import com.github.githubpeon.peon.PeonTaskConcurrencyException;
import com.github.githubpeon.peon.PeonTaskState;
import com.github.githubpeon.peon.event.PeonTaskEvent;
import com.github.githubpeon.peon.event.PeonTaskListener;
import com.github.githubpeon.peon.event.PeonTaskEvent.EventId;
import com.github.githubpeon.peon.swing.SwingPeonMaster;
import com.github.githubpeon.peon.test.ApplicationBlockingTask;
import com.github.githubpeon.peon.test.CancelTask;
import com.github.githubpeon.peon.test.CategoryBlockingTask;
import com.github.githubpeon.peon.test.ClassBlockingTask;
import com.github.githubpeon.peon.test.EndlessTask;
import com.github.githubpeon.peon.test.ErrorTask;
import com.github.githubpeon.peon.test.ExceptionTask;
import com.github.githubpeon.peon.test.SuccessTask;

@RunWith(JUnit4.class)
public class SwingPeonTest implements PeonTaskListener {

    private static PeonMaster peonMaster;
    private Thread workerThread;
    private Thread uiEventThread;

    @BeforeClass
    public static void setUpClass() {
        peonMaster = new SwingPeonMaster();
    }

    @Before
    public void setUp() {
        workerThread = null;
        uiEventThread = null;
    }

    @Test
    public void success() {
        peonMaster.addPeonTaskListener(this);
        PeonTask successTask = new SuccessTask();
        peonMaster.execute(successTask);

        // Pause the test for a short time so the task has time to run and provide its results.
        // This kind of test might not be 100% deterministic even if the time we pause seems generous but hey...
        pause(1000);

        // We want the task to have finished, to have run in a different thread than it
        // was started and, since this function is not run in the ui event thread, that the
        // events from the task were received on a third thread. Note that in an actual
        // Swing application the uiEventThread will be equal to the current thread.
        assertTrue(successTask.getState() == PeonTaskState.FINISHED);
        assertNotNull(workerThread);
        assertNotNull(uiEventThread);
        assertFalse(workerThread == Thread.currentThread());
        assertFalse(uiEventThread == Thread.currentThread());
        assertFalse(workerThread == uiEventThread);
    }

    @Test
    public void cancel() {
        peonMaster.addPeonTaskListener(this);
        PeonTask cancelTask = new CancelTask();
        peonMaster.execute(cancelTask);

        pause(1000);

        assertTrue(cancelTask.getState() == PeonTaskState.CANCELLED);
        assertNotNull(workerThread);
        assertNotNull(uiEventThread);
        assertFalse(workerThread == Thread.currentThread());
        assertFalse(uiEventThread == Thread.currentThread());
        assertFalse(workerThread == uiEventThread);
    }

    @Test
    public void error() {
        peonMaster.addPeonTaskListener(this);
        PeonTask errorTask = new ErrorTask();
        peonMaster.execute(errorTask);

        pause(1000);

        assertTrue(errorTask.getState() == PeonTaskState.FAILED);
        assertNotNull(errorTask.getError());
        assertNotNull(workerThread);
        assertNotNull(uiEventThread);
        assertFalse(workerThread == Thread.currentThread());
        assertFalse(uiEventThread == Thread.currentThread());
        assertFalse(workerThread == uiEventThread);
    }

    @Test
    public void exception() {
        peonMaster.addPeonTaskListener(this);
        PeonTask exceptionTask = new ExceptionTask();
        peonMaster.execute(exceptionTask);

        pause(1000);

        assertTrue(exceptionTask.getState() == PeonTaskState.EXCEPTION);
        assertNotNull(exceptionTask.getException());
        assertNotNull(workerThread);
        assertNotNull(uiEventThread);
        assertFalse(workerThread == Thread.currentThread());
        assertFalse(uiEventThread == Thread.currentThread());
        assertFalse(workerThread == uiEventThread);
    }

    @Test
    public void blockingTaskFor() {
        // We'll use temporary peon masters here so that we won't have to wait for the tasks running in other
        // threads to actually be removed from the peon master.
        PeonMaster tmpPeonMaster = new SwingPeonMaster();

        ApplicationBlockingTask applicationBlockingTask = new ApplicationBlockingTask();
        tmpPeonMaster.execute(applicationBlockingTask);

        assertEquals(applicationBlockingTask, tmpPeonMaster.getBlockingTaskFor(ApplicationBlockingTask.class));
        assertEquals(applicationBlockingTask, tmpPeonMaster.getBlockingTaskFor(CategoryBlockingTask.class));
        assertEquals(applicationBlockingTask, tmpPeonMaster.getBlockingTaskFor(ClassBlockingTask.class));
        assertEquals(applicationBlockingTask, tmpPeonMaster.getBlockingTaskFor(EndlessTask.class));

        applicationBlockingTask.cancel();

        tmpPeonMaster = new SwingPeonMaster();
        CategoryBlockingTask categoryBlockingTask = new CategoryBlockingTask();
        tmpPeonMaster.execute(categoryBlockingTask);

        assertEquals(categoryBlockingTask, tmpPeonMaster.getBlockingTaskFor(ApplicationBlockingTask.class));
        assertEquals(categoryBlockingTask, tmpPeonMaster.getBlockingTaskFor(CategoryBlockingTask.class));
        assertEquals(categoryBlockingTask, tmpPeonMaster.getBlockingTaskFor(ClassBlockingTask.class));
        assertNull(tmpPeonMaster.getBlockingTaskFor(EndlessTask.class));

        categoryBlockingTask.cancel();

        tmpPeonMaster = new SwingPeonMaster();
        ClassBlockingTask classBlockingTask = new ClassBlockingTask();
        tmpPeonMaster.execute(classBlockingTask);

        assertEquals(classBlockingTask, tmpPeonMaster.getBlockingTaskFor(ApplicationBlockingTask.class));
        assertEquals(classBlockingTask, tmpPeonMaster.getBlockingTaskFor(CategoryBlockingTask.class));
        assertEquals(classBlockingTask, tmpPeonMaster.getBlockingTaskFor(ClassBlockingTask.class));
        assertNull(tmpPeonMaster.getBlockingTaskFor(EndlessTask.class));

        classBlockingTask.cancel();

        tmpPeonMaster = new SwingPeonMaster();
        EndlessTask endlessTask = new EndlessTask();
        tmpPeonMaster.execute(endlessTask);

        assertEquals(endlessTask, tmpPeonMaster.getBlockingTaskFor(ApplicationBlockingTask.class));
        assertNull(tmpPeonMaster.getBlockingTaskFor(CategoryBlockingTask.class));
        assertNull(tmpPeonMaster.getBlockingTaskFor(ClassBlockingTask.class));
        assertNull(tmpPeonMaster.getBlockingTaskFor(EndlessTask.class));

        endlessTask.cancel();

    }

    @Test(expected = PeonTaskConcurrencyException.class)
    public void applicationBlock() {
        PeonMaster tmpPeonMaster = new SwingPeonMaster();
        tmpPeonMaster.execute(new ApplicationBlockingTask());
        tmpPeonMaster.execute(new ApplicationBlockingTask());
    }

    @Test(expected = PeonTaskConcurrencyException.class)
    public void categoryBlock() {
        PeonMaster tmpPeonMaster = new SwingPeonMaster();
        tmpPeonMaster.execute(new CategoryBlockingTask());
        tmpPeonMaster.execute(new CategoryBlockingTask());
    }

    @Test(expected = PeonTaskConcurrencyException.class)
    public void classBlock() {
        PeonMaster tmpPeonMaster = new SwingPeonMaster();
        tmpPeonMaster.execute(new ClassBlockingTask());
        tmpPeonMaster.execute(new ClassBlockingTask());
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalTaskStarter() {
        peonMaster.getBlockingTaskFor(new String());
    }

    @Test
    public void taskStarter() {
        PeonMaster tmpPeonMaster = new SwingPeonMaster();

        TaskStartingAction taskStartingAction = new TaskStartingAction();

        assertNull(tmpPeonMaster.getBlockingTaskFor(taskStartingAction));

        CategoryBlockingTask categoryBlockingTask = new CategoryBlockingTask();
        tmpPeonMaster.execute(categoryBlockingTask);

        assertEquals(categoryBlockingTask, tmpPeonMaster.getBlockingTaskFor(taskStartingAction));

        categoryBlockingTask.cancel();
    }

    @AfterClass
    public static void tearDownClass() {
        peonMaster = null;
    }

    @Override
    public void peonTaskEvent(PeonTaskEvent e) {
        if(e.getId() == EventId.CANCELLED
           || e.getId() == EventId.FAILED
           || e.getId() == EventId.EXCEPTION
           || e.getId() == EventId.FINISHED) {
            uiEventThread = Thread.currentThread();
            // Cancelled, exception and error tasks wouldn't normally have results but this is kind
            // of a special case. And doing it this way also tests that the correct events are fired
            // and received on the correct threads in those cases as well.
            workerThread = (Thread)e.getSource().getResult();
        }
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
            } catch(InterruptedException e) {}
    }
}
