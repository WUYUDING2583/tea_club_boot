DROP TABLE IF EXISTS `enterprise`;
CREATE TABLE `enterprise` (
                          `uid` int(11) NOT NULL AUTO_INCREMENT,
                          `name` varchar(20) default null,
                          `contact` varchar(15) default null,
                          `email` varchar(50) default null,
                          `address` varchar(500) default null,
                          PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
