package com.imook.miaosha.redis;

public class GoodsKey extends BasePrefix{

    // 页面缓存的有效期一般比较短expireSeconds
	private GoodsKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}
	public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
}
