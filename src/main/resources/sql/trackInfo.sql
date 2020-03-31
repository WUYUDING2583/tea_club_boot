DROP TABLE IF EXISTS `trackInfo`;
CREATE TABLE `trackInfo` (
                      `uid` int(11) NOT NULL AUTO_INCREMENT,
                      `companyName` varchar(255) DEFAULT NULL,
                      `trackingId` varchar(50) DEFAULT NULL,
                      `description` varchar(200) default null,
                      `phone` varchar(11) default null,
                      PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;