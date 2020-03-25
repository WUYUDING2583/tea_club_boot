DROP TABLE IF EXISTS `mutexActivity`;
CREATE TABLE `mutexActivity` (
                              `uid` int(11) NOT NULL AUTO_INCREMENT,
                              `activityId` int(11) default null,
                              `mutexActivityId` int(11) default null,
                              FOREIGN KEY (`activityId`) REFERENCES activity(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                              FOREIGN KEY (`mutexActivityId`) REFERENCES activity(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                              PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;