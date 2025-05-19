-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema INT221_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema INT221_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `INT221_db` DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci ;
USE `INT221_db` ;

-- -----------------------------------------------------
-- Table `INT221_db`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INT221_db`.`products` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(60) NOT NULL,
  `description` VARCHAR(400) NULL,
  `stock` INT(10) NOT NULL,
  `price` INT(10) NOT NULL,
  `brand` VARCHAR(45) NOT NULL,
  `createdOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INT221_db`.`brand`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INT221_db`.`brand` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL ,
  `countryOfOrigin` VARCHAR(80) NULL ,
  `webSiteUrl` VARCHAR(40) NULL ,
  `isActive` TINYINT NULL, 
  `noOfSaleItems` INT NULL,
  `createdOn` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INT221_db`.`sale_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INT221_db`.`sale_items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `brandId` INT NOT NULL,
  `model` VARCHAR(60) NOT NULL ,
  `description` VARCHAR(400) NOT NULL ,
  `quantity` INT NOT NULL DEFAULT 1,
  `price` INT NOT NULL,
  `screenSizeInch` DECIMAL(7,2) NULL,
  `ramGb` INT NULL,
  `storageGb` INT NULL,
  `color` VARCHAR(45) NULL CHECK (color <> TRIM(color) = ''),
  `createdOn` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `fk_saleItem_brand1_idx` (`brandId` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_saleItem_brand1`
    FOREIGN KEY (`brandId`)
    REFERENCES `INT221_db`.`brand` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
