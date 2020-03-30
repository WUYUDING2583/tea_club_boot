DROP TABLE IF EXISTS `orderProduct`;
CREATE TABLE `orderProduct` (
                                `uid` int(11) NOT NULL AUTO_INCREMENT,
                                `productId` int(11) DEFAULT NULL,
                                `orderId` int(11) default null,
                                `activityRuleId` int(11) default null,
                                `number` int default null,
                                FOREIGN KEY (`activityRuleId`) REFERENCES activityRule(uid),
                                FOREIGN KEY (`productId`) REFERENCES product(uid),
                                FOREIGN KEY (`orderId`) REFERENCES orders(uid),
                                PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;