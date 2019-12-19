package com.special.camunda.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName SchedualService
 * @Description TODO
 * @Author 王亮
 * @Date 2019/12/18 8:59
 */
@Slf4j
@Component
public class SchedualService implements JavaDelegate {

    @Resource
    private ScheduleClient scheduleClient;

    private static final String SCHEDULE_CENTER_PREFIX = "scheduleCenter:";

    private static final String SERVICE_NAME = "service";

    private static final String SERVICE_FUNCTION = "function";

    /**
     *
     * prefix：xx // 模块名称
     * type:1//微服务方式 2 -北向接口方式
     * service:"" // 微服务名
     * function:"" //子url名称
     * param:{}
     * 执行的方法
     * @param execution
     */
    @Override
    public void execute(DelegateExecution execution) {
        if (execution.getVariable(SERVICE_NAME) == null || execution.getVariable(SERVICE_FUNCTION) == null) {
            log.error("service or function is null，return");
            return;
        }

        String service = execution.getVariable("service").toString();
        String function = execution.getVariable("function").toString();
        String param = execution.getVariable("param").toString();
        JSONObject object = new JSONObject();

        if (execution.getVariable("expire") == null) {
            log.error("expire is null，return");
            return;
        }
        Float expire = (Float) execution.getVariable("expire");
        object.put("expire", expire*1000);
        object.put("service", service);
        object.put("function", function);
        object.put("param", param);
        String key = SCHEDULE_CENTER_PREFIX + object.toJSONString();
        JSONObject request = new JSONObject();
        request.put("key", key);

        scheduleClient.addSchedual(request);
        log.info("发送请求:{}", key);
    }
}
