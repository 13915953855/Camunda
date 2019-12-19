package com.special.camunda.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * @ClassName MicroService
 * @Description TODO
 * @Author 王亮
 * @Date 2019/12/17 11:04
 */
@Slf4j
@Component
public class MicroService implements JavaDelegate {

    @Autowired
    RestTemplate restTemplate;

    private static final String URL_PREFIX = "http://";

    private static final String SERVICE_NAME = "service";

    private static final String SERVICE_FUNCTION = "function";

    /**
     * 执行的方法
     * @param execution
     */
    @Override
    public void execute(DelegateExecution execution){

        if (execution.getVariable(SERVICE_NAME) == null || execution.getVariable(SERVICE_FUNCTION) == null) {
            log.error("service or function is null，return");
            return;
        }

        String service = execution.getVariable("service").toString();
        String function = execution.getVariable("function").toString();

        Map<String, Object> variables = execution.getVariables();
        log.info("Variables:{}", JSON.toJSONString(variables));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> formEntity = new HttpEntity<>(JSON.toJSONString(variables), headers);
        String url = new StringBuilder().append(URL_PREFIX).append(service).append(function).toString();
        try {
            JSONObject respone = restTemplate.postForObject(url, formEntity, JSONObject.class);
            log.info("request Url:{}，respone：{}", url, respone.toJSONString());
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
    }

}
