DROP TABLE IF EXISTS `cartDetail`;
CREATE TABLE `cartDetail` (
                       `uid` int(11) NOT NULL AUTO_INCREMENT,
                       `customerId` int(11) default null,
                       `productId` int(11) default null,
                       `number` int default 0,
                       FOREIGN KEY (`customerId`) REFERENCES customer(uid),
                       FOREIGN KEY (`productId`) REFERENCES product(uid),
                       PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
