package com.imook.miaosha.service;

import com.imook.miaosha.dao.MiaoshaUserDao;
import com.imook.miaosha.domain.MiaoshaUser;
import com.imook.miaosha.exception.GlobalException;
import com.imook.miaosha.redis.MiaoshaUserKey;
import com.imook.miaosha.redis.RedisService;
import com.imook.miaosha.result.CodeMsg;
import com.imook.miaosha.util.MD5Util;
import com.imook.miaosha.util.UUIDUtil;
import com.imook.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoShaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getMiaoshaUserByID(long id){

        // 对象缓存 取缓存
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.getById,"" + id,MiaoshaUser.class);
        // 取缓存
        if(miaoshaUser != null ){
            return miaoshaUser;
        }
        // 否则就要取数据库
        miaoshaUser = miaoshaUserDao.getMiaoshaUserByID(id);
        if(miaoshaUser != null ){
            redisService.set(MiaoshaUserKey.getById,"" + id,miaoshaUser);
        }
        return miaoshaUser;
    }

    // 密码修改
    public boolean updatePwd(String token,long id,String passwordNew){
        MiaoshaUser miaoshaUser = getMiaoshaUserByID(id);
        if(miaoshaUser == null ){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 更新这个对象
        // 更新一个对象一般新创建一个对象 效率更高
        // 更新数据库
        // 对象缓存一定要考虑是不是有更新数据的地方  一定要记得更新缓存 不然就会造成数据不一致
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew,miaoshaUser.getSalt()));
        miaoshaUserDao.update(toBeUpdate);

        // 重要的是要更新缓存
        // 先删除缓存
        redisService.delete(MiaoshaUserKey.getById,"" + id);
        // 注意不能直接删除token，不然没法登陆了
        miaoshaUser.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,miaoshaUser);
        return true;

    }

    public String login(HttpServletResponse response,LoginVo loginVo) {
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        // 获取表单密码
        String formpwd = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        // 判断手机号是否存在
        MiaoshaUser miaoshaUser =  getMiaoshaUserByID(Long.parseLong(mobile));
        if(miaoshaUser == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 验证密码  获取db里面的密码
        String dbPass = miaoshaUser.getPassword();
        String slatDB = miaoshaUser.getSalt();

        // 最后计算出来的密码
        String calpwd = MD5Util.formPassToDBPass(formpwd,slatDB);
        if(!calpwd.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASS_ERROR);
        }
//        // 生成cookie
//        String token = UUIDUtil.uuid();
//        // 把用户信息放在缓存中 以后直接取token就是用户信息的key
//        redisService.set(MiaoshaUserKey.token,token,miaoshaUser);
//        // 设置cookie的key-value
//        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
//
//        // cookie有效期  为了保持一致性 MiaoshaUserKey.token过期 cookie就过期
//        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
//        cookie.setPath("/");
//
//        // cookie写入客户端
//        response.addCookie(cookie);
        // 生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response,token,miaoshaUser);
        return token;
    }

    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        // 判断非空
        if(StringUtils.isEmpty(token)){
            return null;
        }
        // 从缓存得到数据
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);

        // 延长一下有效期 就是重新生成一个cookie然后返回
        // 非空判断
        if (miaoshaUser != null){
            addCookie(response,token,miaoshaUser);
        }
        return miaoshaUser;
    }

    private void addCookie(HttpServletResponse response,String token,MiaoshaUser user){
        // 生成cookie  不需要每次都新生成一个cookie
        // String token = UUIDUtil.uuid();
        // 把用户信息放在缓存中 以后直接取token就是用户信息的key
        redisService.set(MiaoshaUserKey.token,token,user);
        // 设置cookie的key-value
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);

        // cookie有效期  为了保持一致性 MiaoshaUserKey.token过期 cookie就过期
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");

        // cookie写入客户端
        response.addCookie(cookie);
    }
}
