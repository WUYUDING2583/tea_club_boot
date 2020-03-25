DROP TABLE IF EXISTS `photo`;
CREATE TABLE `photo` (
                       `uid` int(11) NOT NULL AUTO_INCREMENT,
                       `photo` MEDIUMBLOB DEFAULT  NULL,
                       `shopId` int(11) default null,
                       `clerkId` int(11) default null,
                       `shopBoxId` int(11) default null,
                       `productId` int(11) default null,
                       FOREIGN KEY (`shopBoxId`) REFERENCES shopBox(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                       FOREIGN KEY (`productId`) REFERENCES product(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                       FOREIGN KEY (`shopId`) REFERENCES shop(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                       FOREIGN KEY (`clerkId`) REFERENCES clerk(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                      PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

