package com.imook.miaosha.service;

import com.imook.miaosha.domain.MiaoshaUser;
import com.imook.miaosha.domain.OrderInfo;
import com.imook.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoShaService {

    // 一般来说其他service不引用自己的dao，使用别的dao  都是引用别的层的service
    // OrderDao orderDao;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    // 开启事务
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        // 减库存
        goodsService.reduceStock(goods);
        // 创建订单下订单 写入秒杀订单
        return orderService.createOrder(user,goods);

    }
}
