package com.imook.miaosha.dao;

import com.imook.miaosha.domain.Goods;
import com.imook.miaosha.domain.MiaoshaGoods;
import com.imook.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@PathVariable("goodsId") long goodsId);

    // 减少库存
    @Update("Update miaosha_goods set stock_count = stock_count-1 where goods_id = #{goodsId}")
    public void reduceStock(MiaoshaGoods g);
}
