DROP TABLE IF EXISTS `activityRule2`;
CREATE TABLE `activityRule2` (
                                         `uid` int(11) NOT NULL AUTO_INCREMENT,
                                         `activityRuleId` int(11) default null,
                                         `number` float default null,
                                         `currency` varchar(6) default null,
                                         `operation` varchar(4) default null,
                                         FOREIGN KEY (`activityRuleId`) REFERENCES activityRule(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                                         PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;