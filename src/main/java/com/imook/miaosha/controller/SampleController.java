package com.imook.miaosha.controller;

import com.imook.miaosha.domain.User;
import com.imook.miaosha.redis.RedisService;
import com.imook.miaosha.redis.UserKey;
import com.imook.miaosha.result.Result;
import com.imook.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;


    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","xiaoqutongxue");
        return "hello";
    }
    @RequestMapping("/db/Get")
    @ResponseBody
    public Result<User> dbGet(){
        return Result.success(userService.getById(1));
    }

    /*
     *  事务
     */
    @RequestMapping("/db/Tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.Tx();
        return Result.success(true);
    }

    /**
     * 缓存redis
     */
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User  user  = redisService.get(UserKey.getById, ""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user  = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return Result.success(true);
    }

}
