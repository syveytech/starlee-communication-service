CREATE DATABASE  IF NOT EXISTS `starlee` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `starlee`;
-- MySQL dump 10.13  Distrib 5.7.19, for Linux (x86_64)
--
-- Host: localhost    Database: starlee
-- ------------------------------------------------------
-- Server version	5.7.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `first_name` varchar(150) DEFAULT NULL,
  `last_name` varchar(150) DEFAULT NULL,
  `user_mobile_no` varchar(10) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` (`first_name`, `last_name`, `user_mobile_no`, `created_date`, `modified_date`) VALUES ('Manoj',NULL,'8457846401','2018-12-28 07:23:21','2018-12-28 07:23:21'),('Sagarika',NULL,'9437672385','2018-12-28 07:23:22','2018-12-28 07:23:22'),('Surath',NULL,'9964258384','2018-12-28 07:23:22','2018-12-28 07:23:22'),('Chandana',NULL,'9886285750','2018-12-28 07:23:23','2018-12-28 07:23:23');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_sms_notification_records`
--

DROP TABLE IF EXISTS `email_sms_notification_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_sms_notification_records` (
  `user_id` int(10) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `email_recipients` varchar(500) DEFAULT NULL,
  `sms_recipients` varchar(10) DEFAULT NULL,
  `is_email_sent` int(1) DEFAULT NULL,
  `is_sms_sent` int(1) DEFAULT NULL,
  `event_name` varchar(45) DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_sms_notification_records`
--

LOCK TABLES `email_sms_notification_records` WRITE;
/*!40000 ALTER TABLE `email_sms_notification_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_sms_notification_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_sms_notification_records_test`
--

DROP TABLE IF EXISTS `email_sms_notification_records_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_sms_notification_records_test` (
  `first_name` varchar(50) NOT NULL,
  `user_mobile_no` varchar(10) DEFAULT NULL,
  `is_sms_sent` int(1) DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_sms_notification_records_test`
--

LOCK TABLES `email_sms_notification_records_test` WRITE;
/*!40000 ALTER TABLE `email_sms_notification_records_test` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_sms_notification_records_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parameters`
--

DROP TABLE IF EXISTS `parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parameters` (
  `parameter_name` varchar(45) NOT NULL DEFAULT '',
  `parameter_value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`parameter_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parameters`
--

LOCK TABLES `parameters` WRITE;
/*!40000 ALTER TABLE `parameters` DISABLE KEYS */;
INSERT INTO `parameters` (`parameter_name`, `parameter_value`) VALUES ('BULKSMS_API_KEY','mkbehera.se@gmail.com'),('BULKSMS_API_TOKEN','a6b5c56ba155c47a944ebeee576444e91545913629'),('BULKSMS_API_URL','https://www.bulksmsplans.com/Restapis/send_sms?'),('BULKSMS_MSG_TYPE','1'),('BULKSMS_SENDER','MSGBST'),('CRON_EXP_EMAIL_JOB','0 0/1 * * * ?'),('CRON_EXP_SMS_JOB','0 0/1 * 1/1 * ? *'),('EDIT_USER_EMAIL_RECEIPT_SUBJECT','Recharge Portal Email Id Change'),('EMAIL_FROM_ADDRESS','noreply@starlee.com'),('EMAIL_RETRY_COUNT','1'),('EMAIL_TEMPLATE_PATH','/appserver/retailservice/config/resources/email/templates'),('FORGET_PASSWORD_EMAIL_RECEIPT_SUBJECT','Hotel StarLee Reset Password URL'),('IS_SEND_EMAIL_ACTIVE','0'),('IS_SEND_SMS_ACTIVE','1'),('MAXIMUM_PERIOD_FOR_EXPIRE','3'),('ON_LOGIN_SUCCESS_OTP_LENGTH','4'),('ON_LOGIN_SUCCESS_SMS_TEMPLATE','<OTP> is your OTP for logging into Wibmo Recharge Portal.'),('PROXY_IP','172.31.2.76'),('PROXY_PORT','3128'),('PWD_KEY_ALIAS','\'W1I2B3M4O5\''),('RECIPIENT_LIST','manoj.behera@wibmo.com'),('RESET_PASSWORD_LINK','http://localhost:3030/RechargePortal/resetPassword'),('SMS_HOST','192.168.104.10'),('SMS_JOB_BATCH_SIZE_FOR_FETCHING_RECORDS','100'),('SMS_PORT','8025'),('SMS_PROMOTIONS_TEMPLATE','Dear customer, Post Christmas dine are still live around New Year. Show this text to get 20% off on total billing at Hotel StarLee, Telecom Layout, Jakkur.'),('SMS_RETRY_COUNT','3'),('SMTP_HOST','192.168.104.10'),('SP_CONNECTION_TIME_OUT','30000'),('SP_READ_TIME_OUT','30000'),('VERIFY_MOBILE_NUMBER_OTP_LENGTH','6'),('VERIFY_MOBILE_NUMBER_SMS_TEMPLATE','<OTP> is your OTP to verify your phone number for Wibmo Recharge portall.');
/*!40000 ALTER TABLE `parameters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_code`
--

DROP TABLE IF EXISTS `status_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_status` int(11) DEFAULT NULL,
  `job_status_narration` varchar(50) DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_code`
--

LOCK TABLES `status_code` WRITE;
/*!40000 ALTER TABLE `status_code` DISABLE KEYS */;
INSERT INTO `status_code` (`id`, `job_status`, `job_status_narration`, `created_date`, `modified_date`) VALUES (1,2000,'oplookup change initiated','2018-08-16 17:05:23','2018-08-16 17:05:23'),(2,2001,'oplookup change approved','2018-08-16 17:05:23','2018-08-16 17:05:23'),(3,2002,'oplookup change rejected','2018-08-16 17:05:23','2018-08-16 17:05:23'),(4,2003,'oplookup change expired','2018-08-16 17:05:23','2018-10-04 10:16:37'),(5,2004,'tagged for update and cache refresh','2018-08-16 17:05:24','2018-10-08 11:51:46'),(6,2005,'oplookup updated and cache refreshed','2018-10-04 10:16:37','2018-10-04 12:34:37');
/*!40000 ALTER TABLE `status_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_registration`
--

DROP TABLE IF EXISTS `user_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_registration` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `user_password` varchar(300) DEFAULT NULL,
  `role_id` int(10) DEFAULT NULL,
  `is_active` int(1) DEFAULT '0',
  `email_id` varchar(150) DEFAULT NULL,
  `first_name` varchar(150) DEFAULT NULL,
  `last_name` varchar(150) DEFAULT NULL,
  `user_mobile_no` varchar(10) NOT NULL,
  `is_mobile_no_verified` int(1) DEFAULT '0',
  `is_email_verified` int(1) DEFAULT '0',
  `skip_otp` int(1) DEFAULT '0',
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_name`),
  KEY `user_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_registration`
--

LOCK TABLES `user_registration` WRITE;
/*!40000 ALTER TABLE `user_registration` DISABLE KEYS */;
INSERT INTO `user_registration` (`user_id`, `user_name`, `user_password`, `role_id`, `is_active`, `email_id`, `first_name`, `last_name`, `user_mobile_no`, `is_mobile_no_verified`, `is_email_verified`, `skip_otp`, `created_date`, `modified_date`) VALUES (1,'admin','STBnFnD+QP4=',1,1,'mkbehera.se@gmail.com','Manoj','Behera','8457846434',1,1,1,'2018-03-28 19:12:33','2018-12-26 07:22:31'),(2,'foo','8o09OMXwSVM=',2,1,'mkbehera.se@gmail.com','RefundUser','User','8596589652',1,1,1,'2018-09-30 20:06:00','2018-12-26 07:22:58'),(3,'harish','T3s2BkfvQ4ac3hhEynDucA==',4,1,'mkbehera.se@gmail.com','Manoj','k','8457846401',1,1,0,'2018-10-15 03:52:46','2018-12-26 07:22:58'),(4,'john','7TBuwAxuh3E=',3,1,'mkbehera.se@gmail.com','RefundAdmin','Admin','8596589652',1,1,1,'2018-09-30 20:01:34','2018-12-26 07:22:31'),(5,'manoj','STBnFnD+QP4=',4,0,'mkbehera.se@gmail.com','dddd','ddd','8457846405',0,0,0,'2018-10-15 04:24:57','2018-12-26 07:22:58'),(6,'sachin','LTpknFN3C/8=',5,1,'mkbehera.se@gmail.com','RoutingOrderAdmin','Admin','7829422884',1,1,1,'2018-09-30 19:44:19','2018-12-26 07:22:58'),(7,'stela','7IHvA5cJOf0=',4,1,'rouser@gmail.com','RoutingOrderUser','User','8596589652',1,1,1,'2018-09-30 19:59:09','2018-12-26 07:22:58');
/*!40000 ALTER TABLE `user_registration` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-28 13:28:33
