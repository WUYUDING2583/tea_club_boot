DROP TABLE IF EXISTS `activityApplyForProduct`;
CREATE TABLE `activityApplyForProduct` (
                              `uid` int(11) NOT NULL AUTO_INCREMENT,
                              `activityRuleId` int(11) default null,
                              `productId` int(11) default null,
                              FOREIGN KEY (`activityRuleId`) REFERENCES activityRule(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                              FOREIGN KEY (`productId`) REFERENCES product(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                              PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;