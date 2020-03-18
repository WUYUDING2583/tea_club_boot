DROP TABLE IF EXISTS `shopBox`;
CREATE TABLE `shopBox` (
                      `uid` int(11) NOT NULL AUTO_INCREMENT,
                      `name` varchar(255) DEFAULT NULL,
                      `description` varchar(255) DEFAULT NULL,
                      `shopId` int(11) DEFAULT NULL,
                      `boxNum` varchar(10) DEFAULT NULL,
                      `price` FLOAT default NULL,
                      `duration` int DEFAULT NULL,
                      FOREIGN KEY (`shopId`) REFERENCES shop(uid),
                      PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;