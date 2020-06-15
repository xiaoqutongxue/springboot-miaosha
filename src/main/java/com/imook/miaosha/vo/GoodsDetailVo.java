package com.imook.miaosha.vo;

import com.imook.miaosha.domain.MiaoshaUser;

public class GoodsDetailVo {
    private GoodsVo goods;
    private MiaoshaUser user;
    // 定义一个状态
    private int miaoshaStatus = 0;

    // 计算剩余秒杀开始时间
    private int remainSeconds = 0;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    @Override
    public String toString() {
        return "GoodsDetailVo{" +
                "goods=" + goods +
                ", user=" + user +
                ", miaoshaStatus=" + miaoshaStatus +
                ", remainSeconds=" + remainSeconds +
                '}';
    }
}
