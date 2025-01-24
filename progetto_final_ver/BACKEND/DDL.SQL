DROP TABLE IF EXISTS `Log`;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `Finger`;

CREATE TABLE `Log` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `state` INTEGER NOT NULL,
    `date` DATETIME NOT NULL,
    `fk_user` INTEGER,
    `fk_finger` INTEGER,
    `device` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `User` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `fk_finger` INTEGER,
    `name` VARCHAR(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `Finger` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `fk_user` INTEGER NOT NULL,
    `value` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `Log` ADD FOREIGN KEY (`fk_user`) REFERENCES `User`(`id`);
ALTER TABLE `Log` ADD FOREIGN KEY (`fk_finger`) REFERENCES `Finger`(`id`);
ALTER TABLE `Finger` ADD FOREIGN KEY (`fk_user`) REFERENCES `User`(`id`);
