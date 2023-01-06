CREATE TABLE `post`
(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `author` varchar(255),
    `title` varchar(255),
    `content` varchar(255),
    `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);