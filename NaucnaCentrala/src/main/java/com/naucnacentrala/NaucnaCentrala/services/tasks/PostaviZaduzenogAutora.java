package com.naucnacentrala.NaucnaCentrala.services.tasks;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class PostaviZaduzenogAutora implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        String autor = (String) delegateTask.getExecution().getVariable("autor");
        delegateTask.setAssignee(autor);
    }
}
