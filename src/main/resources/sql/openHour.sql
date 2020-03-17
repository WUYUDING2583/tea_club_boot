# 门店开放时间段表
DROP TABLE IF EXISTS `openHour`;
CREATE TABLE `openHour` (
                       `uid` int(11) NOT NULL AUTO_INCREMENT,
                       `startTime` varchar(10) DEFAULT NULL,
                       `endTime`  varchar(10) DEFAULT NULL,
                       `shopId`int(11),
                       FOREIGN KEY (shopId) REFERENCES shop(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                       PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

