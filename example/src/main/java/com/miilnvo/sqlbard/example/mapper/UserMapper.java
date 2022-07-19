package com.miilnvo.sqlbard.example.mapper;

import com.miilnvo.sqlbard.example.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author MiilnVo
 * @since 2022-07-15
 */
@Mapper
public interface UserMapper {

    List<User> selectList();

    User selectOne(User user);

}
