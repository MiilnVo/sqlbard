DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `t_integer` int(20) NOT NULL,
  `t_float` float(255,2) DEFAULT NULL,
  `t_double` double(255,3) DEFAULT NULL,
  `t_long` bigint(255) DEFAULT NULL,
  `t_char` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `t_bytes` blob,
  `t_string` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `t_bigdecimal` decimal(65,0) DEFAULT NULL,
  `t_gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `t_enabled` tinyint(1) DEFAULT NULL,
  `t_year` year(4) DEFAULT NULL,
  `t_date` date DEFAULT NULL,
  `t_time` time DEFAULT NULL,
  `t_localdate` date DEFAULT NULL,
  `t_localtime` time DEFAULT NULL,
  `t_localdatetime` datetime DEFAULT NULL,
  `t_timestamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`t_integer`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;