DROP TABLE IF EXISTS `activityApplyForCustomerTypes`;
CREATE TABLE `activityApplyForCustomerTypes` (
                                         `uid` int(11) NOT NULL AUTO_INCREMENT,
                                         `activityRuleId` int(11) default null,
                                         `customerTypeId` int(11) default null,
                                         FOREIGN KEY (`activityRuleId`) REFERENCES activityRule(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                                         FOREIGN KEY (`customerTypeId`) REFERENCES customerType(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                                         PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;