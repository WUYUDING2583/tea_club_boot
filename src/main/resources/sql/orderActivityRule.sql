DROP TABLE IF EXISTS `orderActivityRule`;
CREATE TABLE `orderActivityRule` (
                              `uid` int(11) NOT NULL AUTO_INCREMENT,
                              `activityRuleId` int(11) DEFAULT NULL,
                              `orderId` int(11) default null,
                              FOREIGN KEY (`activityRuleId`) REFERENCES activityRule(uid),
                              FOREIGN KEY (`orderId`) REFERENCES orders(uid),
                              PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;