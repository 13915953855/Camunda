package com.special.camunda.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ServiceTask implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(ServiceTask.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> variables = delegateExecution.getVariables();
        log.info("variables is {}", variables);

        log.info("success execute service task!");
    }
}
