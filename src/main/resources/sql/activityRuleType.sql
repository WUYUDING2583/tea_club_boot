DROP TABLE IF EXISTS `activityRuleType`;
CREATE TABLE `activityRuleType` (
                              `uid` int(11) NOT NULL AUTO_INCREMENT,
                              `name` varchar(20) default null,
                              PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
insert into activityRuleType(name) values("充值");
insert into activityRuleType(name) values("购物");
insert into activityRuleType(name) values("折扣");
insert into activityRuleType(name) values("分享");
insert into activityRuleType(name) values("阅读");