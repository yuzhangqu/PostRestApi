CREATE TABLE `comment`
(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `post_id` bigint,
    `commenter` varchar(255),
    `content` varchar(255),
    `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);