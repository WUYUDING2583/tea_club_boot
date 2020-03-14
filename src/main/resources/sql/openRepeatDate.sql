DROP TABLE IF EXISTS `openRepeatDate`;
CREATE TABLE `openRepeatDate` (
                          `uid` int(11) NOT NULL AUTO_INCREMENT,
                          `date` int DEFAULT NULL,
                          `openHourId` int(11),
                          FOREIGN KEY (`openHourId`) REFERENCES openHour(uid),
                          PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

