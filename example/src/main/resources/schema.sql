CREATE TABLE user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(32) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  age int(11) DEFAULT NULL,
  birthday date DEFAULT NULL,
  registerTime timestamp NULL DEFAULT NULL,
  enabled tinyint(1) DEFAULT NULL,
  PRIMARY KEY (id)
);