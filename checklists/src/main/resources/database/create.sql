-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema checklists
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema checklists
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `checklists` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `checklists` ;

-- -----------------------------------------------------
-- Table `checklists`.`chk_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `checklists`.`chk_user` ;

CREATE TABLE IF NOT EXISTS `checklists`.`chk_user` (
  `id` CHAR(36) NOT NULL,
  `email` VARCHAR(128) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(128) NOT NULL,
  `last_name` VARCHAR(128) NOT NULL,
  `company` VARCHAR(128) NULL,
  `date_created` DATETIME NOT NULL,
  `date_modified` DATETIME NOT NULL,
  `token` VARCHAR(36) NULL,
  `token_last_access` DATETIME NULL,
  `token_expired` TINYINT(1) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `checklists`.`checklist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `checklists`.`checklist` ;

CREATE TABLE IF NOT EXISTS `checklists`.`checklist` (
  `id` CHAR(36) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `date_created` DATETIME NOT NULL,
  `date_modified` DATETIME NOT NULL,
  `created_by_user_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_checklist_chk_user`
    FOREIGN KEY (`created_by_user_id`)
    REFERENCES `checklists`.`chk_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_checklist_chk_user_idx` ON `checklists`.`checklist` (`created_by_user_id` ASC);


-- -----------------------------------------------------
-- Table `checklists`.`task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `checklists`.`task` ;

CREATE TABLE IF NOT EXISTS `checklists`.`task` (
  `id` CHAR(36) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `date_created` DATETIME NOT NULL,
  `date_modified` DATETIME NOT NULL,
  `date_expires` DATETIME NULL,
  `date_reminder` DATETIME NULL,
  `description` TEXT NULL,
  `completed` TINYINT(1) NOT NULL,
  `checklist_id` CHAR(36) NOT NULL,
  `parent_task_id` CHAR(36) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_task_checklist1`
    FOREIGN KEY (`checklist_id`)
    REFERENCES `checklists`.`checklist` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_task_task1`
    FOREIGN KEY (`parent_task_id`)
    REFERENCES `checklists`.`task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_task_checklist1_idx` ON `checklists`.`task` (`checklist_id` ASC);

CREATE INDEX `fk_task_task1_idx` ON `checklists`.`task` (`parent_task_id` ASC);


-- -----------------------------------------------------
-- Table `checklists`.`issue`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `checklists`.`issue` ;

CREATE TABLE IF NOT EXISTS `checklists`.`issue` (
  `id` CHAR(36) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `description` TEXT NULL,
  `date_created` DATETIME NOT NULL,
  `date_modified` DATETIME NOT NULL,
  `date_resolved` DATETIME NULL,
  `resolved` TINYINT(1) NOT NULL,
  `task_id` CHAR(36) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_issue_task1`
    FOREIGN KEY (`task_id`)
    REFERENCES `checklists`.`task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_issue_task1_idx` ON `checklists`.`issue` (`task_id` ASC);


-- -----------------------------------------------------
-- Table `checklists`.`note`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `checklists`.`note` ;

CREATE TABLE IF NOT EXISTS `checklists`.`note` (
  `id` CHAR(36) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `text` TEXT NOT NULL,
  `date_created` DATETIME NOT NULL,
  `date_modified` DATETIME NOT NULL,
  `task_id` CHAR(36) NULL,
  `issue_id` CHAR(36) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_note_task1`
    FOREIGN KEY (`task_id`)
    REFERENCES `checklists`.`task` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_note_issue1`
    FOREIGN KEY (`issue_id`)
    REFERENCES `checklists`.`issue` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_note_task1_idx` ON `checklists`.`note` (`task_id` ASC);

CREATE INDEX `fk_note_issue1_idx` ON `checklists`.`note` (`issue_id` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
