SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `SensorDB` ;
CREATE SCHEMA IF NOT EXISTS `SensorDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `SensorDB` ;

-- -----------------------------------------------------
-- Table `SensorDB`.`SensorNodes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SensorDB`.`SensorNodes` ;

CREATE  TABLE IF NOT EXISTS `SensorDB`.`SensorNodes` (
  `SensorId` VARCHAR(45) NOT NULL ,
  `SensorType` VARCHAR(45) NULL ,
  `Description` VARCHAR(10000) NULL ,
  `SensorImage` LONGBLOB NULL ,
  `Timestamp` TIMESTAMP NULL ,
  PRIMARY KEY (`SensorId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SensorDB`.`NetworkMap`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SensorDB`.`NetworkMap` ;

CREATE  TABLE IF NOT EXISTS `SensorDB`.`NetworkMap` (
  `sensId` VARCHAR(45) NULL ,
  `nodeId` VARCHAR(45) NULL ,
  `zoneId` VARCHAR(45) NULL )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SensorDB`.`BaseStation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SensorDB`.`BaseStation` ;

CREATE  TABLE IF NOT EXISTS `SensorDB`.`BaseStation` (
  `bsId` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(1000) NULL ,
  `name` VARCHAR(45) NULL ,
  `ip` VARCHAR(45) NULL ,
  PRIMARY KEY (`bsId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SensorDB`.`Zone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SensorDB`.`Zone` ;

CREATE  TABLE IF NOT EXISTS `SensorDB`.`Zone` (
  `zoneId` VARCHAR(45) NOT NULL ,
  `name` VARCHAR(45) NULL ,
  `description` VARCHAR(45) NULL ,
  `edificio` VARCHAR(45) NULL ,
  `piano` VARCHAR(45) NULL ,
  PRIMARY KEY (`zoneId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SensorDB`.`Measures`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SensorDB`.`Measures` ;

CREATE  TABLE IF NOT EXISTS `SensorDB`.`Measures` (
  `SensorId` VARCHAR(45) NOT NULL ,
  `id_meas` VARCHAR(45) NOT NULL ,
  `data` LONGBLOB NULL ,
  `date` VARCHAR(45) NULL ,
  `time` VARCHAR(45) NULL ,
  PRIMARY KEY (`id_meas`) )
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
