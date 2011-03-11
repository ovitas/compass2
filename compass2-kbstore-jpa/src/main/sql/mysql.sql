-- MySQL dump 10.13  Distrib 5.1.42, for Win64 (unknown)
--
-- Host: localhost    Database: compass2_kbstore_jpa
-- ------------------------------------------------------
-- Server version	5.1.42-community

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
-- Table structure for table `direct_relation`
--

DROP TABLE IF EXISTS `direct_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `direct_relation` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `END_TOPIC_ID` bigint(20) NOT NULL,
  `DIRECT_RELATION_TYPE_ID` bigint(20) DEFAULT NULL,
  `START_TOPIC_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `START_TOPIC_ID` (`START_TOPIC_ID`,`END_TOPIC_ID`, `DIRECT_RELATION_TYPE_ID`),
  KEY `FK272DE592F7B7C042` (`START_TOPIC_ID`),
  KEY `FK272DE5925FABB54D` (`DIRECT_RELATION_TYPE_ID`),
  KEY `FK272DE592F453F3E9` (`END_TOPIC_ID`),
  CONSTRAINT `FK272DE592F453F3E9` FOREIGN KEY (`END_TOPIC_ID`) REFERENCES `topic` (`ID`),
  CONSTRAINT `FK272DE5925FABB54D` FOREIGN KEY (`DIRECT_RELATION_TYPE_ID`) REFERENCES `direct_relation_type` (`ID`),
  CONSTRAINT `FK272DE592F7B7C042` FOREIGN KEY (`START_TOPIC_ID`) REFERENCES `topic` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `direct_relation_type`
--

DROP TABLE IF EXISTS `direct_relation_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `direct_relation_type` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTIVE` bit(1) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  `EXTERNAL_ID` varchar(255) NOT NULL,
  `GENERALIZATION_WEIGHT` double NOT NULL,
  `OCCURRENCE` int(11) DEFAULT NULL,
  `WEIGHT` double NOT NULL,
  `KNOWLEDGE_BASE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DISPLAY_NAME` (`DISPLAY_NAME`,`KNOWLEDGE_BASE_ID`),
  KEY `FK114E0DC7AB12D0A2` (`KNOWLEDGE_BASE_ID`),
  CONSTRAINT `FK114E0DC7AB12D0A2` FOREIGN KEY (`KNOWLEDGE_BASE_ID`) REFERENCES `knowledge_base` (`ID`),
  INDEX(`EXTERNAL_ID`),
  INDEX(`KNOWLEDGE_BASE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `knowledge_base`
--

DROP TABLE IF EXISTS `knowledge_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `knowledge_base` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `DISPLAY_NAME` varchar(100) NOT NULL,
  `ACTIVE` bit(1) DEFAULT NULL,
  `TYPE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DISPLAY_NAME` (`DISPLAY_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scope`
--

DROP TABLE IF EXISTS `scope`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scope` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DISPLAY_NAME` varchar(255) NOT NULL,
  `EXTERNAL_ID` varchar(255) NOT NULL,
  `KNOWLEDGE_BASE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DISPLAY_NAME` (`DISPLAY_NAME`,`KNOWLEDGE_BASE_ID`),
  KEY `FK6833E54AB12D0A2` (`KNOWLEDGE_BASE_ID`),
  CONSTRAINT `FK6833E54AB12D0A2` FOREIGN KEY (`KNOWLEDGE_BASE_ID`) REFERENCES `knowledge_base` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `topic`
--

DROP TABLE IF EXISTS `topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topic` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EXTERNAL_ID` varchar(255) NOT NULL,
  `KNOWLEDGE_BASE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `EXTERNAL_ID` (`EXTERNAL_ID`,`KNOWLEDGE_BASE_ID`),
  KEY `FK696CD2FAB12D0A2` (`KNOWLEDGE_BASE_ID`),
  CONSTRAINT `FK696CD2FAB12D0A2` FOREIGN KEY (`KNOWLEDGE_BASE_ID`) REFERENCES `knowledge_base` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `topic_name`
--

DROP TABLE IF EXISTS `topic_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topic_name` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `SCOPE_ID` bigint(20) DEFAULT NULL,
  `TOPIC_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKCE434B9B3F408725` (`SCOPE_ID`),
  KEY `FKCE434B9B7EE38505` (`TOPIC_ID`),
  CONSTRAINT `FKCE434B9B7EE38505` FOREIGN KEY (`TOPIC_ID`) REFERENCES `topic` (`ID`),
  CONSTRAINT `FKCE434B9B3F408725` FOREIGN KEY (`SCOPE_ID`) REFERENCES `scope` (`ID`),
  INDEX(`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

