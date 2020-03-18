DROP TABLE IF EXISTS `clerk`;
CREATE TABLE `clerk` (
                       `uid` int(11) NOT NULL AUTO_INCREMENT,
                       `name` varchar(20) DEFAULT  NULL,
                       `positionId` int(11) default null,
                       `contact` varchar(15) default null,
                       `identityId` varchar(20) default null,
                       `sex` int default null,
                       `address` varchar(200) default null,
                       `shopId` int(11) default null,
                       FOREIGN KEY (`positionId`) REFERENCES position(uid),
                       FOREIGN KEY (`shopId`) REFERENCES shop(uid),
                       PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
