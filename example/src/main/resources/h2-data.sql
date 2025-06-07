INSERT INTO user (t_integer, t_float, t_double, t_long, t_char, t_bytes, t_string,
                  t_bigdecimal, t_gender, t_enabled, t_year, t_date, t_time,
                  t_localdate, t_localtime, t_localdatetime, t_timestamp)
VALUES (1, 1.23, 4.56, 1234567890, 'char1', X'627974657331', 'string1',
        12345678901234567890, 'MALE', 1, 2022, '2022-07-15', '10:30:45',
        '2022-07-15', '10:30:45', '2022-07-15 10:30:45', '2022-07-15 10:30:45');

-- 插入第二条数据
INSERT INTO user (t_integer, t_float, t_double, t_long, t_char, t_bytes, t_string,
                  t_bigdecimal, t_gender, t_enabled, t_year, t_date, t_time,
                  t_localdate, t_localtime, t_localdatetime, t_timestamp)
VALUES (2, 2.34, 5.67, 9876543210, 'char2', X'627974657332', 'string2',
        98765432109876543210, 'WOMAN', 0, 2021, '2021-06-10', '11:15:30',
        '2021-06-10', '11:15:30', '2021-06-10 11:15:30', '2021-06-10 11:15:30');