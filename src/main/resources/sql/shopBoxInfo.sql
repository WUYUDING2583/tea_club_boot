DROP TABLE IF EXISTS `shopBoxInfo`;
CREATE TABLE `shopBoxInfo` (
                         `uid` int(11) NOT NULL AUTO_INCREMENT,
                         `title` varchar(255) DEFAULT NULL,
                         `info` varchar(255) DEFAULT NULL,
                         `boxId` int(11) DEFAULT NULL,
                         FOREIGN KEY (`boxId`) REFERENCES shopBox(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                         PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;