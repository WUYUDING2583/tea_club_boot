DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
                       `uid` int(11) NOT NULL AUTO_INCREMENT,
                       `name` varchar(20) DEFAULT  NULL,
                       `url` varchar(200) default null,
                       PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
