DROP TABLE IF EXISTS `customerAddress`;
CREATE TABLE `customerAddress` (
                          `uid` int(11) NOT NULL AUTO_INCREMENT,
                          `address` varchar(200) default null,
                          `customerId` int(11) default null,
                          `isDefault` tinyint default false,
                          PRIMARY KEY (`uid`),
                          FOREIGN KEY (`customerId`) REFERENCES customer(`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;