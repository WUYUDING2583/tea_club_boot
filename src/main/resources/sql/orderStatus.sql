DROP TABLE IF EXISTS `orderStatus`;
CREATE TABLE `orderStatus` (
                              `uid` int(11) NOT NULL AUTO_INCREMENT,
                              `orderId` int(11) DEFAULT NULL,
                              `status` varchar(20) default null,
                              `time` bigint default null,
                              FOREIGN KEY (`orderId`) REFERENCES orders(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                              PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;