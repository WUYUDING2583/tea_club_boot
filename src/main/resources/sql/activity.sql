DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
    `uid` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(50) default null,
    `description` varchar(200) default null,
    `startTime` BIGINT default null,
    `endTime` bigint default null,
    `enforceTerminal` bool default false,
    PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;