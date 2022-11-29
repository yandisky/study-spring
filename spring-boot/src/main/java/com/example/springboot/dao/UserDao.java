package com.example.springboot.dao;

import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {
    User findUserByName(String name);

    @Select("select * from user")
    List<User> findAllUser();

    @Insert("insert into user (name,age,money) values (#{name},#{age},#{money})")
    void insertUser(@Param("name") String name, @Param("age") Integer age, @Param("money") Double money);

    @Update("update user set name = #{name}, age = #{age}, money = #{money} where id = #{id}")
    void updateUser(@Param("name") String name, @Param("age") Integer age, @Param("money") Double money, @Param("id") Integer id);

    @Delete("delete from user where id = #{id}")
    void deleteUser(@Param("id") Integer id);
}
