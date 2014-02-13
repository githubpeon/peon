package org.peon.test.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.peon.annotation.StartsTasks;
import org.peon.test.CategoryBlockingTask;

@StartsTasks(tasks = CategoryBlockingTask.class)
public class TaskStartingAction extends AbstractAction {

    private static final long serialVersionUID = -6243216889218141678L;

    @Override
    public void actionPerformed(ActionEvent e) {}
    
}
