package com.imook.miaosha.controller;


import com.imook.miaosha.domain.MiaoshaUser;
import com.imook.miaosha.redis.GoodsKey;
import com.imook.miaosha.redis.RedisService;
import com.imook.miaosha.result.Result;
import com.imook.miaosha.service.GoodsService;
import com.imook.miaosha.service.MiaoShaUserService;
import com.imook.miaosha.vo.GoodsDetailVo;
import com.imook.miaosha.vo.GoodsVo;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    MiaoShaUserService miaoShaService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    // required = false  意思是有可能没有的
    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
//    public String toGoodList(Model model, @CookieValue(value = MiaoShaUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
////                             @RequestParam(value = MiaoShaUserService.COOKIE_NAME_TOKEN,required = false) String paramToken,
////                             HttpServletResponse response){
    // 简单实现 WebConfig 进行配置
    public String toGoodList(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user){

//        // 两者都是空的话就返回登录页面
//        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
//            return "login";
//        }
//
//        // 优先级判断 paramToken为空就取cookieToken，paramToken不为空就取paramToken
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//
//        // 取出cookie做成秒杀user
//        MiaoshaUser user = miaoShaService.getByToken(response,token);
        model.addAttribute("user",user);
        // 缓存页面
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        // 取缓存
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsVoList);
        // return "goods_list";

        // 手动渲染
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);

        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    // 拓展对于id而言 可以采用一个算法设置id，一般不会用自增长的id。算法是：snowflake
    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toDetail2(HttpServletRequest request, HttpServletResponse response,Model model,
                           MiaoshaUser user, @PathVariable("goodsId") long goodsId){
        model.addAttribute("user",user);
        // 缓存页面
        String html = redisService.get(GoodsKey.getGoodsDetail,"" + goodsId,String.class);
        // 取缓存
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        // 获取商品的详细信息
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        // 获取秒杀的开始时间和结束时间和现在时...getTime()转换成毫秒
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        // 定义一个状态
        int miaoshaStatus = 0;

        // 计算剩余秒杀开始时间
        int remainSeconds = 0;

        if(now < startAt){ // 秒杀没有开始  应该倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now)/1000);

        }else if(now > endAt){ // 秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;

        }else{ // 秒杀正在进行
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        // return "goods_detail";

        // 手动渲染
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);

        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,"" + goodsId,html);
        }
        return html;
    }


    /**
     * 详情页面的静态化
     *goods_detail.html -->goods_detail.htm  因为配置文件配置了找html
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(HttpServletRequest request, HttpServletResponse response, Model model,
                                          MiaoshaUser user, @PathVariable("goodsId") long goodsId){
        // 获取商品的详细信息
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        // 获取秒杀的开始时间和结束时间和现在时...getTime()转换成毫秒

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        // 定义一个状态
        int miaoshaStatus = 0;

        // 计算剩余秒杀开始时间
        int remainSeconds = 0;

        if(now < startAt){ // 秒杀没有开始  应该倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now)/1000);

        }else if(now > endAt){ // 秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;

        }else{ // 秒杀正在进行
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        return Result.success(goodsDetailVo);
    }
}
