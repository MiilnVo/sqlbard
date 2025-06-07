package com.miilnvo.sqlbard.example;

import com.miilnvo.sqlbard.example.mapper.UserMapper;
import com.miilnvo.sqlbard.example.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
class SqlbardExampleApplicationTests {

    @Resource
    private UserMapper userMapper;

	@Test
	void example1() {
        List<User> users = userMapper.selectList();
        log.info("users: {}", users);
    }

    @Test
    void example2() throws Exception {
        User user = new User();

        user.setTInteger(1);
        user.setTFloat(new Float("2.22"));
        user.setTDouble(new Double("3.333"));
        user.setTLong(new Long("99999"));
        user.setTChar('a');

        byte[] b1 = new byte[]{'8'};
        user.setTBytes(b1);

        user.setTString("hello");
        user.setTBigDecimal(new BigDecimal("500"));
        user.setTGender(User.Gender.MALE);
        user.setTEnabled(true);
        user.setTYear(Year.of(2022));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2022-07-19");
        user.setTDate(date);

        user.setTTime(Time.valueOf("14:15:54"));

        user.setTLocalDate(LocalDate.of(2022,7,19));
        user.setTLocalTime(LocalTime.of(14,15,58));

        LocalDateTime localDateTime = LocalDateTime.parse("2022-07-19 14:16:01", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        user.setTLocalDateTime(localDateTime);

        user.setTTimestamp(new Timestamp(1658211363000L));

        User result = userMapper.selectOne(user);

        log.info("user: {}", result);
    }

}
