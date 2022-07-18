package com.miilnvo.sqlbard.example.pojo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author MiilnVo
 * @since 2022-07-15
 */
@Data
public class User {

    private Long id;

    private String name;

    private String password;

    private Integer age;

    private Date birthday;

    private LocalDateTime registerTime;

    private Boolean enabled;

}
