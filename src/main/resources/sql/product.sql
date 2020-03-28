DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
                       `uid` int(11) NOT NULL AUTO_INCREMENT,
                       `name` varchar(20) default null,
                       `type` int(11) default null,
                       `description` varchar(200) default null,
                       `priceId` int(11) default null,
                       `storage` int default null,
                       `enforceTerminal` bool default false,
                       FOREIGN KEY (`type`) REFERENCES productType(uid),
                       FOREIGN KEY (`priceId`) REFERENCES price(uid),
                       PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;