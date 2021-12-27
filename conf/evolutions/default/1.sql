# --- !Ups
CREATE TABLE IF NOT EXISTS `scalatestdb`.`product` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NULL DEFAULT NULL,
    `isAvailable` TINYINT(4) NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    AUTO_INCREMENT = 2
    DEFAULT CHARACTER SET = utf8

    # --- !Downs
drop table `scalatestdb`.`product`