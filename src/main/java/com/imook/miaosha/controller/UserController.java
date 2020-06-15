package com.imook.miaosha.controller;

import com.imook.miaosha.domain.MiaoshaUser;
import com.imook.miaosha.redis.RedisService;
import com.imook.miaosha.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/user")
public class UserController {


    // docker exec -it 8b66ff32341c redis-benchmark -h 127.0.0.1 -p 6379 -c 100 -n 10000
    // docker 调用redis自带压测工具
	@Autowired
    RedisService redisService;
	
    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user) {
        return Result.success(user);
    }
    
}
