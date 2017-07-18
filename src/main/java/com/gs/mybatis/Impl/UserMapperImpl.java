package com.gs.mybatis.Impl;

import com.gs.mybatis.bean.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapperImpl {

    @Insert("insert into user values(#{id},#{name},#{age})")
    public int AddUser(User user);

    @Select("select * from user where id=#{id}")
    public User retrieveUser(int id);

    @Select("select * from user")
    public List<User> retrieveAllUser();

    @Update("update user set name=#{name} where id=#{id}")
    public int updateUser(User user);

    @Delete("delete from user where name=#{name}")
    public int deleteUser(String name);

}
