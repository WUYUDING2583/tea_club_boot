DROP TABLE IF EXISTS `recharge`;
CREATE TABLE `recharge` (
                             `uid` int(11) NOT NULL AUTO_INCREMENT,
                             `amount` float default 0.0,
                             `time` bigint default null,
                             `customerId` int(11) default null,
                             PRIMARY KEY (`uid`),
                             FOREIGN KEY (`customerId`) REFERENCES customer(`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;