package com.imook.miaosha.controller;

import com.imook.miaosha.result.Result;
import com.imook.miaosha.service.MiaoShaUserService;
import com.imook.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    MiaoShaUserService miaoShaService;

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/do_login")
    @ResponseBody
    /**
     * jsr303校验
     * @Valid注解的引入
     */
    public Result<String> doLogin(HttpServletResponse response,@Valid LoginVo loginVo){
        // 接收到参数
        log.info(loginVo.toString());
//        // 进行参数校验
//        String pwd = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//        // 密码非空
//        if(StringUtils.isEmpty(pwd)){
//            return Result.error(CodeMsg.PWD_ISEmpty);
//        }
//        // 手机号非空
//        if(StringUtils.isEmpty(mobile)){
//            return Result.error(CodeMsg.MOBILE_ISEmpty);
//        }
//        // 判断手机号是否正确
//        if(!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }

        // 登录
        String token = miaoShaService.login(response,loginVo);
        return Result.success(token);
    }
}
