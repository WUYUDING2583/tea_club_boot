DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
                              `uid` int(11) NOT NULL AUTO_INCREMENT,
                              `name` varchar(20) default null,
                              `contact` varchar(15) default null,
                              `identityId` varchar(20) default null,
                              `email` varchar(50) default null,
                              `type` int(11) default null,
                              `gender` int default null,
                              `password` varchar(30) default null,
                              `weChatId` varchar(20) default null,
                              `address` varchar(500) default null,
                              FOREIGN KEY (`type`) REFERENCES customerType(uid) ON UPDATE CASCADE,
                              PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;