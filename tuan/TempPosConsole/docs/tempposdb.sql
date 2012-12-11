/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.1.62-community : Database - tempposdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP DATABASE IF EXISTS tempposdb;

CREATE DATABASE IF NOT EXISTS tempposdb DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON tempposdb.* TO posdba@localhost IDENTIFIED BY 'posdba';

USE `tempposdb`;

/*Table structure for table `temp_category` */

DROP TABLE IF EXISTS `temp_category`;

CREATE TABLE `temp_category` (
  `category_id` int(6) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(32) NOT NULL,
  `category_desc` text NOT NULL,
  `category_form` varchar(128) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `temp_category` */

/*Table structure for table `temp_firm` */

DROP TABLE IF EXISTS `temp_firm`;

CREATE TABLE `temp_firm` (
  `firm_id` int(6) NOT NULL AUTO_INCREMENT,
  `firm_name` varchar(32) NOT NULL,
  `firm_desc` text NOT NULL,
  `firm_address` varchar(255) NOT NULL,
  `firm_phone` varchar(24) NOT NULL,
  `firm_person` varchar(32) NOT NULL,
  PRIMARY KEY (`firm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `temp_firm` */

/*Table structure for table `temp_key` */

DROP TABLE IF EXISTS `temp_key`;

CREATE TABLE `temp_key` (
  `key_id` int(6) NOT NULL AUTO_INCREMENT,
  `tuan_id` int(6) NOT NULL,
  `key_code` char(24) NOT NULL,
  `key_status` int(1) NOT NULL,
  PRIMARY KEY (`key_id`),
  UNIQUE KEY `NewIndex1` (`key_code`),
  KEY `FK_Reference_3` (`tuan_id`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`tuan_id`) REFERENCES `temp_tuan` (`tuan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `temp_key` */

/*Table structure for table `temp_log` */

DROP TABLE IF EXISTS `temp_log`;

CREATE TABLE `temp_log` (
  `log_id` int(6) NOT NULL AUTO_INCREMENT,
  `user_id` int(6) DEFAULT NULL,
  `log_time` bigint(13) NOT NULL,
  `log_content` text NOT NULL,
  PRIMARY KEY (`log_id`),
  KEY `FK_Reference_4` (`user_id`),
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`user_id`) REFERENCES `temp_users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `temp_log` */

/*Table structure for table `temp_tuan` */

DROP TABLE IF EXISTS `temp_tuan`;

CREATE TABLE `temp_tuan` (
  `tuan_id` int(6) NOT NULL AUTO_INCREMENT,
  `category_id` int(6) NOT NULL,
  `firm_id` int(6) NOT NULL,
  `tuan_name` varchar(32) NOT NULL,
  `tuan_desc` text NOT NULL,
  `tuan_starttime` bigint(21) NOT NULL,
  `tuan_endtime` bigint(21) NOT NULL,
  PRIMARY KEY (`tuan_id`),
  KEY `FK_Reference_1` (`category_id`),
  KEY `FK_Reference_2` (`firm_id`),
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`firm_id`) REFERENCES `temp_firm` (`firm_id`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`category_id`) REFERENCES `temp_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `temp_tuan` */

/*Table structure for table `temp_users` */

DROP TABLE IF EXISTS `temp_users`;

CREATE TABLE `temp_users` (
  `user_id` int(6) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(32) NOT NULL,
  `user_pass` varchar(32) NOT NULL,
  `user_grade` int(1) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `temp_users` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
