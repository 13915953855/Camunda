package com.special.camunda.controller;

import com.alibaba.fastjson.JSONObject;
import com.special.camunda.feign.HelloClient;
import com.special.camunda.process.SimpleProcessHandler;
import com.special.camunda.request.TaskRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 徐森
 * @CreateDate: 2019/12/19
 * @Description:
 */
@RestController
@Slf4j
public class CallbackController {
    @Autowired
    private SimpleProcessHandler simpleProcessHandler;

    @PostMapping("/callback")
    public void callback(@RequestBody JSONObject jsonObject){
        log.info("触发回调事件");
        String pass = jsonObject.getString("pass");
        String processInstanceId = jsonObject.getString("processInstanceId");
        Map<String,Object> variables = new HashMap<>();
        variables.put("pass",pass);
        variables.put("processInstanceId",processInstanceId);
        TaskRequest request = new TaskRequest();
        request.setProcessInstId(processInstanceId);
        request.setVariables(variables);
        try {
            simpleProcessHandler.mandualApproveProcess(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
