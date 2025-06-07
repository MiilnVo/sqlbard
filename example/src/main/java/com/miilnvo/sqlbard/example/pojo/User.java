package com.miilnvo.sqlbard.example.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.Date;

/**
 * @author MiilnVo
 * @since 2022-07-15
 */
@Data
public class User {

    private Integer tInteger;

    private Float tFloat;

    private Double tDouble;

    private Long tLong;

    private Character tChar;

    private byte[] tBytes;

    private String tString;

    private BigDecimal tBigDecimal;

    private Gender tGender;

    private Boolean tEnabled;

    private Year tYear;

    private Date tDate;

    private Time tTime;

    private LocalDate tLocalDate;

    private LocalTime tLocalTime;

    private LocalDateTime tLocalDateTime;

    private Timestamp tTimestamp;

    public enum Gender {
        MALE, WOMAN
    }

}
