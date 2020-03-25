DROP TABLE IF EXISTS `activityRule`;
CREATE TABLE `activityRule` (
                          `uid` int(11) NOT NULL AUTO_INCREMENT,
                          `typeId` int(11) default null,
                          `activityRule1` float default null,
                          PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;