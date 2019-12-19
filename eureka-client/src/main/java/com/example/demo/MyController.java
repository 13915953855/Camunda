package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 徐森
 * @CreateDate: 2019/12/19
 * @Description:
 */
@RestController
public class MyController {

    @PostMapping("/hello")
    public JSONObject sayHello(@RequestBody JSONObject request){
        System.out.println("hello");
        JSONObject result = new JSONObject();
        result.put("code",200);
        result.put("message","恭喜你，调用微服务成功！");
        result.put("content",request);
        return result;
    }
}
