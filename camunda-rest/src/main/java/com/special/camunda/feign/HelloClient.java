package com.special.camunda.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Author: 徐森
 * @CreateDate: 2019/12/19
 * @Description:
 */
@FeignClient(value = "MICROSERVICE")
public interface HelloClient {
    @PostMapping("/hello")
    public void sayHello();
}
