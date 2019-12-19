package com.special.camunda.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 定时器
 */

@FeignClient(value = "SMARTPANEL-PANEL-SCHEDULE")
public interface ScheduleClient {

    @PostMapping(value = "/service/addSchedual/Millis")
    JSONObject addSchedual(@RequestBody JSONObject jsonObject) ;
}