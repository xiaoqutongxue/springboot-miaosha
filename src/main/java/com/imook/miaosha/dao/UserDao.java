package com.imook.miaosha.dao;

import com.imook.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import javax.websocket.server.PathParam;

@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getUserById(@PathParam("id") int id);

    @Insert("insert into user(id,name) values (#{id},#{name})")
    public int insert(User user);
}
