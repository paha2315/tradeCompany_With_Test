-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema course_work
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema course_work
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `course_work` DEFAULT CHARACTER SET cp1251 ;
USE `course_work` ;

-- -----------------------------------------------------
-- Table `course_work`.`okei_code`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`okei_code` (
  `ID_OKEI` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`ID_OKEI`),
  UNIQUE INDEX `ID_OKEI_UNIQUE` (`ID_OKEI` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = cp1251;


-- -----------------------------------------------------
-- Table `course_work`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`product` (
  `ID_Product` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(128) NOT NULL,
  `Description` VARCHAR(512) NULL DEFAULT NULL,
  `Nomenclature` VARCHAR(30) NOT NULL,
  `Article` VARCHAR(30) NULL DEFAULT NULL,
  `ID_OKEI` INT UNSIGNED NOT NULL,
  `Mass` DOUBLE NOT NULL,
  `PercentNDS` INT UNSIGNED NOT NULL,
  `StorageConditions` VARCHAR(1024) NULL DEFAULT NULL,
  `Deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID_Product`),
  UNIQUE INDEX `ID_Product_UNIQUE` (`ID_Product` ASC) VISIBLE,
  INDEX `ID_OKEI_idx` (`ID_OKEI` ASC) VISIBLE,
  CONSTRAINT `ID_OkeiP`
    FOREIGN KEY (`ID_OKEI`)
    REFERENCES `course_work`.`okei_code` (`ID_OKEI`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = cp1251;


-- -----------------------------------------------------
-- Table `course_work`.`warehouse`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`warehouse` (
  `ID_Warehouse` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(128) NOT NULL,
  `City` VARCHAR(64) NOT NULL,
  `Street` VARCHAR(64) NOT NULL,
  `BuildingNumber` INT UNSIGNED NOT NULL,
  `BuildingLiteral` CHAR(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_Warehouse`),
  UNIQUE INDEX `ID_Warehouse_UNIQUE` (`ID_Warehouse` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = cp1251;


-- -----------------------------------------------------
-- Table `course_work`.`availability`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`availability` (
  `ID_Availability` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_Product` INT UNSIGNED NOT NULL,
  `ID_Warehouse` INT UNSIGNED NOT NULL,
  `SectionNumber` INT UNSIGNED NOT NULL,
  `StackNumber` INT UNSIGNED NOT NULL,
  `Count` DOUBLE NULL DEFAULT '0',
  `Price` DOUBLE NULL DEFAULT '0',
  `PackageType` VARCHAR(30) NOT NULL,
  `PackageWidth` DOUBLE NULL DEFAULT NULL,
  `PackageHeight` DOUBLE NULL DEFAULT NULL,
  `PackageDepth` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`ID_Availability`),
  UNIQUE INDEX `ID_Availability_UNIQUE` (`ID_Availability` ASC) VISIBLE,
  INDEX `ID_Product_idx` (`ID_Product` ASC) VISIBLE,
  INDEX `ID_Warehouse_idx` (`ID_Warehouse` ASC) VISIBLE,
  CONSTRAINT `ID_ProductA`
    FOREIGN KEY (`ID_Product`)
    REFERENCES `course_work`.`product` (`ID_Product`),
  CONSTRAINT `ID_WarehouseA`
    FOREIGN KEY (`ID_Warehouse`)
    REFERENCES `course_work`.`warehouse` (`ID_Warehouse`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = cp1251;


-- -----------------------------------------------------
-- Table `course_work`.`supplier`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`supplier` (
  `ID_Supplier` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_Product` INT UNSIGNED NOT NULL,
  `Name` VARCHAR(128) NOT NULL,
  `City` VARCHAR(32) NOT NULL,
  `AddressFull` VARCHAR(128) NULL DEFAULT NULL,
  `OKPO` INT UNSIGNED NOT NULL,
  `PaymentAccount` VARCHAR(20) NOT NULL,
  `Phone` VARCHAR(20) NOT NULL,
  `ZipCode` VARCHAR(6) NOT NULL,
  `BankInfo` VARCHAR(64) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_Supplier`),
  UNIQUE INDEX `ID_Supplier_UNIQUE` (`ID_Supplier` ASC) VISIBLE,
  INDEX `ID_Product_idx` (`ID_Product` ASC) VISIBLE,
  CONSTRAINT `ID_ProductS`
    FOREIGN KEY (`ID_Product`)
    REFERENCES `course_work`.`product` (`ID_Product`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = cp1251;


-- -----------------------------------------------------
-- Table `course_work`.`post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`post` (
  `ID_Post` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`ID_Post`),
  UNIQUE INDEX `idroles_UNIQUE` (`ID_Post` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `course_work`.`person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`person` (
  `ID_Person` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_Post` INT UNSIGNED NOT NULL,
  `ID_Supplier` INT UNSIGNED NULL DEFAULT NULL,
  `FirstName` VARCHAR(45) NOT NULL,
  `SecondName` VARCHAR(45) NOT NULL,
  `ThirdName` VARCHAR(45) NULL,
  `Login` VARCHAR(64) NOT NULL,
  `Password` VARCHAR(512) NOT NULL,
  PRIMARY KEY (`ID_Person`),
  UNIQUE INDEX `ID_Person_UNIQUE` (`ID_Person` ASC) VISIBLE,
  INDEX `fk_person_roles1_idx` (`ID_Post` ASC) VISIBLE,
  INDEX `fk_person_supplier1_idx` (`ID_Supplier` ASC) VISIBLE,
  CONSTRAINT `ID_PostP`
    FOREIGN KEY (`ID_Post`)
    REFERENCES `course_work`.`post` (`ID_Post`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_person_supplier1`
    FOREIGN KEY (`ID_Supplier`)
    REFERENCES `course_work`.`supplier` (`ID_Supplier`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `course_work`.`document`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`document` (
  `ID_Document` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Date` DATE NOT NULL,
  PRIMARY KEY (`ID_Document`),
  UNIQUE INDEX `ID_Document_UNIQUE` (`ID_Document` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `course_work`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`orders` (
  `ID_Order` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_Document` INT UNSIGNED NULL,
  `ID_Supplier` INT UNSIGNED NOT NULL,
  `ID_Warehouse` INT UNSIGNED NOT NULL,
  `Price` DOUBLE NOT NULL,
  `Count` DOUBLE NOT NULL,
  `Completed` TINYINT NOT NULL DEFAULT 0,
  INDEX `fk_document_has_supplier_supplier1_idx` (`ID_Supplier` ASC) VISIBLE,
  INDEX `fk_document_has_supplier_document1_idx` (`ID_Document` ASC) VISIBLE,
  INDEX `fk_order_from_warehouse_warehouse1_idx` (`ID_Warehouse` ASC) VISIBLE,
  PRIMARY KEY (`ID_Order`),
  UNIQUE INDEX `ID_Order_UNIQUE` (`ID_Order` ASC) VISIBLE,
  CONSTRAINT `ID_DocumentDHS`
    FOREIGN KEY (`ID_Document`)
    REFERENCES `course_work`.`document` (`ID_Document`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ID_SupplierDHS`
    FOREIGN KEY (`ID_Supplier`)
    REFERENCES `course_work`.`supplier` (`ID_Supplier`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_from_warehouse_warehouse1`
    FOREIGN KEY (`ID_Warehouse`)
    REFERENCES `course_work`.`warehouse` (`ID_Warehouse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `course_work`.`availability_link`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_work`.`availability_link` (
  `ID_Link` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `ID_Document` INT UNSIGNED NOT NULL,
  `ID_Availability` INT UNSIGNED NOT NULL,
  `Count` DOUBLE NOT NULL,
  INDEX `fk_document_has_availability_availability1_idx` (`ID_Availability` ASC) VISIBLE,
  INDEX `fk_document_has_availability_document1_idx` (`ID_Document` ASC) VISIBLE,
  PRIMARY KEY (`ID_Link`),
  UNIQUE INDEX `ID_Link_UNIQUE` (`ID_Link` ASC) VISIBLE,
  CONSTRAINT `ID_DocumentDHA`
    FOREIGN KEY (`ID_Document`)
    REFERENCES `course_work`.`document` (`ID_Document`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ID_AvailabilityDHA`
    FOREIGN KEY (`ID_Availability`)
    REFERENCES `course_work`.`availability` (`ID_Availability`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
