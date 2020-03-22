# 门店开发日期表
DROP TABLE IF EXISTS `openRepeatDate`;
CREATE TABLE `openRepeatDate` (
                          `uid` int(11) NOT NULL AUTO_INCREMENT,
                          `date` varchar(2) DEFAULT NULL,
                          `openHourId` int(11),
                          FOREIGN KEY (`openHourId`) REFERENCES openHour(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                          PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

