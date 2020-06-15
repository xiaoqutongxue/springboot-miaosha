package com.imook.miaosha.controller;


import com.imook.miaosha.domain.MiaoshaOrder;
import com.imook.miaosha.domain.MiaoshaUser;
import com.imook.miaosha.domain.OrderInfo;
import com.imook.miaosha.redis.RedisService;
import com.imook.miaosha.result.CodeMsg;
import com.imook.miaosha.service.GoodsService;
import com.imook.miaosha.service.MiaoShaService;
import com.imook.miaosha.service.MiaoShaUserService;
import com.imook.miaosha.service.OrderService;
import com.imook.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/miaosha")
public class MiaoShaController {
    @Autowired
    MiaoShaService miaoShaService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;
    @RequestMapping("/do_miaosha")
    public String miaoSha(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId){

        // 判断用户是不是存在
        if(user == null){
            return "login";
        }
        model.addAttribute("user",user);
        // 判断库存 库存足够才能继续秒杀
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0 ){
            model.addAttribute("errorMsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        // 判断是不是秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null){
            model.addAttribute("errorMsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        /**
         * 秒杀实现：要在事务里面做，秒杀失败回滚所有的操作
         * 1.减去库存
         * 2.下订单
         * 3.写入秒杀订单
         */
        // 创建秒杀方法 返回订单
        OrderInfo orderInfo = miaoShaService.miaosha(user,goods);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);

        // 返回订单详情页
        return "order_detail";
    }

}
