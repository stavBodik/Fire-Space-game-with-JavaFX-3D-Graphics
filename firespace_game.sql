CREATE DATABASE  IF NOT EXISTS firespace /*!40100 DEFAULT CHARACTER SET utf8 */;
USE firespace;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: firespace
-- ------------------------------------------------------
-- Server version	5.5.27-log

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
-- Table structure for table game
--

DROP TABLE IF EXISTS game;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE game (id datetime NOT NULL DEFAULT '0000-00-00 00:00:00',gametype varchar(100) DEFAULT NULL,score int(11) NOT NULL,playeremail varchar(100) DEFAULT NULL,PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table game
--

LOCK TABLES game WRITE;
/*!40000 ALTER TABLE game DISABLE KEYS */;
INSERT INTO game VALUES ('2016-10-12 17:59:24','EASY',84,'stavbodik@gmail.com'),('2016-10-12 18:00:12','MEDIUM',16380,'stavbodik@gmail.com'),('2016-10-12 18:01:20','HARD',120,'stavbodik@gmail.com'),('2016-10-12 18:02:54','EASY',96,'guyy@gmail.com'),('2016-10-12 18:04:51','MEDIUM',1020,'guyy@gmail.com'),('2016-10-12 18:10:29','HARD',120,'guyy@gmail.com'),('2016-10-12 18:11:41','EASY',78,'mayan@gmail.com'),('2016-10-12 18:12:37','MEDIUM',131068,'mayan@gmail.com'),('2016-10-12 18:13:24','MEDIUM',16380,'mayan@gmail.com'),('2016-10-12 18:14:08','HARD',120,'mayan@gmail.com'),('2016-10-12 18:14:52','MEDIUM',130940,'mayan@gmail.com'),('2016-10-12 18:15:44','EASY',73,'mayan@gmail.com'),('2016-10-12 18:16:45','EASY',79,'stavbodik@gmail.com'),('2016-10-12 18:28:10','HARD',265719,'stavbodik@gmail.com'),('2016-10-12 18:29:01','EASY',67,'michaln@gmail.com'),('2016-10-12 18:29:43','HARD',39,'michaln@gmail.com'),('2016-10-12 18:31:07','HARD',265719,'michaln@gmail.com'),('2016-10-12 20:37:41','EASY',100,'tamir@gmail.com'),('2016-10-12 22:41:06','EASY',3,'stavbodik@gmail.com');
/*!40000 ALTER TABLE game ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
commit;
-- Dump completed on 2016-10-13 17:46:27
