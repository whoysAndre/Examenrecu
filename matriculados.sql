CREATE DATABASE IF NOT EXISTS matriculados;
USE matriculados;

DROP TABLE IF EXISTS medico;
CREATE TABLE medico (
  codiMedi int NOT NULL AUTO_INCREMENT,
  ndniMedi varchar(8) DEFAULT NULL,
  appMedi varchar(45) DEFAULT NULL,
  apmaMedi varchar(45) DEFAULT NULL,
  nombMedi varchar(45) DEFAULT NULL,
  fechNaciMedi date DEFAULT NULL,
  logiMedi varchar(100) DEFAULT NULL,
  passMedi varchar(500) DEFAULT NULL,
  PRIMARY KEY (codiMedi)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES medico WRITE;
INSERT INTO medico VALUES (1,'75165901','aquiño','valdez','rodrigo','2003-02-21','rod','$2a$12$eDU5.C3D8XTVdAUhIrig4.ecPil7.vEL3FaaKhnyhHSbvARbtP4qq'),(3,'15744901','garcia','moreno','karla','2025-06-05','karla','$2a$12$GV5N/bLgwW2KX342oQsDFuPcFkSnk5X5DCNELqVPidbK5VUHsnAvm'),(4,'42146686','Paucar','Huaman','Ana','2025-06-11','ana','$2a$12$ZNe5LpLQxtqQH.eIEyR0XeKtBWiHhwTlc2pNqrxdNGZhg9.stDxWm'),(5,'98765601','Palma','Nega','Vilam','2025-06-25','vil','$2a$12$Q.U0jTdRcLWvy/lXMs/dzeUrulBXHTOZ.AZm2P24D4JyBgkqvothO');
UNLOCK TABLES;