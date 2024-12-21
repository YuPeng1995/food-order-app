package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select * from user where email = #{email}")
    User getByEmail(String email);

    @Insert("INSERT INTO user (username, password, email, phone, create_time, update_time, status)" +
            "VALUES (#{username}, #{password}, #{email}, #{phone}, #{createTime}, #{updateTime}, #{status})")
    void insert(User user);

    @Update("UPDATE user SET username = #{username}, password = #{password}, email = #{email}, phone = #{phone}, update_time = #{updateTime}, status = #{status} WHERE id = #{id}")
    void update(User user);
}
