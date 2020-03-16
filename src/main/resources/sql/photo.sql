DROP TABLE IF EXISTS `photo`;
CREATE TABLE `photo` (
                       `uid` int(11) NOT NULL AUTO_INCREMENT,
                       `photo` BLOB DEFAULT  NULL,
                       `shopId` int(11),
                       FOREIGN KEY (`shopId`) REFERENCES shop(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                      PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

