
drop DATABASE if exists  alfadb;

CREATE DATABASE alfadb;

use alfadb;

drop table if exists employees;

drop table if exists salaries;

drop table if exists users;


CREATE TABLE employees (
  id bigint NOT NULL AUTO_INCREMENT,
  address_city varchar(255) DEFAULT NULL,
  address_number varchar(255) DEFAULT NULL,
  address_street varchar(255) DEFAULT NULL,
  address_country varchar(255) DEFAULT NULL,
  is_active varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE salaries (
  id bigint NOT NULL AUTO_INCREMENT,
  gross varchar(255) DEFAULT NULL,
  month varchar(255) DEFAULT NULL,
  tax varchar(255) DEFAULT NULL,
  total varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE users (
  id int NOT NULL,
  username varchar(45) DEFAULT NULL,
  password varchar(45) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
use alfadb;
