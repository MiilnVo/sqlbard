package com.miilnvo.sqlbard.example;

import com.miilnvo.sqlbard.example.mapper.UserMapper;
import com.miilnvo.sqlbard.example.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
class SqlbardExampleApplicationTests {

    @Resource
    private UserMapper userMapper;

	@Test
	void example1() {
        List<User> user = userMapper.selectList();
        log.info("user: {}", user);
    }

}
