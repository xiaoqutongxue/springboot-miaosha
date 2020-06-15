package com.imook.miaosha.dao;

import com.imook.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.websocket.server.PathParam;

@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id = #{id}")
    MiaoshaUser getMiaoshaUserByID(@PathParam("id") long id);

    @Update("update miaosha_user set password = #{password} where id = {#id}")
    void update(MiaoshaUser toBeUpdate);
}
