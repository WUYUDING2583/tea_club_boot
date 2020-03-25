DROP TABLE IF EXISTS `customerType`;
CREATE TABLE `customerType` (
                             `uid` int(11) NOT NULL AUTO_INCREMENT,
                             `name` varchar(20) default null,
                             PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
insert into customerType(name) values("注册用户");
insert into customerType(name) values("充值用户");
insert into customerType(name) values("超级vip用户");
insert into customerType(name) values("企业用户");