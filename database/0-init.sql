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
CREATE SCHEMA IF NOT EXISTS `INT221_db` DEFAULT CHARACTER SET utf8mb4 ;
USE `INT221_db` ;

-- -----------------------------------------------------
-- Table `INT221_db`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INT221_db`.`products` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(60) NOT NULL CHECK (name <> ''),
  `description` VARCHAR(400) NULL CHECK (description <> ''),
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
  `id` INT NOT NULL,
  `name` VARCHAR(30) NOT NULL CHECK (TRIM(name) <> ''),
  `websiteUrl` VARCHAR(40) NULL CHECK (websiteUrl <> ''),
  `isActive` TINYINT NULL DEFAULT 0 ,
  `countryOfOrigin` VARCHAR(80) NULL CHECK (countryOfOrigin <> ''),
  `createdOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INT221_db`.`saleItem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INT221_db`.`saleItem` (
  `id` INT NOT NULL,
  `model` VARCHAR(60) NOT NULL CHECK (TRIM(model) <> ''),
  `description` VARCHAR(400) NOT NULL CHECK (TRIM(description) <> '') ,
  `price` INT NOT NULL,
  `ramGb` INT NULL,
  `screenSizeInch` DECIMAL(7,2) NULL,
  `storageGb` INT NULL,
  `color` VARCHAR(45) NULL CHECK (color <> ''),
  `quantity` INT NOT NULL DEFAULT 1,
  `createdOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `brand_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_saleItem_brand1_idx` (`brand_id` ASC) VISIBLE,
  CONSTRAINT `fk_saleItem_brand1`
    FOREIGN KEY (`brand_id`)
    REFERENCES `INT221_db`.`brand` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
