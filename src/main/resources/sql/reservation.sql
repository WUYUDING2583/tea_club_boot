DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
                             `uid` int(11) NOT NULL AUTO_INCREMENT,
                             `reservationTime` bigint default null ,
                             `boxId` int(11) default null,
                             `orderId` int(11) default null,
                             PRIMARY KEY (`uid`),
                             FOREIGN KEY (`boxId`) REFERENCES shopBox(`uid`),
                             FOREIGN KEY (`orderId`) REFERENCES orders(`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;