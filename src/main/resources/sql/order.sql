DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
                              `uid` int(11) NOT NULL AUTO_INCREMENT,
                              `orderTime` bigint default null,
                              `customerId` int(11) default null,
                              `clerkId` int(11) default null,
                              `activityRuleId` int(11) default null,
                              `status` varchar(20) default null,
                              FOREIGN KEY (`activityRuleId`) REFERENCES activityRule(uid),
                              FOREIGN KEY (`customerId`) REFERENCES customer(uid),
                              FOREIGN KEY (`clerkId`) REFERENCES clerk(uid),
                              PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;