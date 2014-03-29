package com.github.githubpeon.peon.test.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


import com.github.githubpeon.peon.annotation.StartsTasks;
import com.github.githubpeon.peon.test.CategoryBlockingTask;

@StartsTasks(tasks = CategoryBlockingTask.class)
public class TaskStartingAction extends AbstractAction {

    private static final long serialVersionUID = -6243216889218141678L;

    @Override
    public void actionPerformed(ActionEvent e) {}
    
}
