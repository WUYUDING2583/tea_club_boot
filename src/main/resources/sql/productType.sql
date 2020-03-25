DROP TABLE IF EXISTS `productType`;
CREATE TABLE `productType` (
                       `uid` int(11) NOT NULL AUTO_INCREMENT,
                       `name` varchar(20) default null,
                       PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
insert into productType(name) values("文章");
insert into productType(name) values("普洱茶");
insert into productType(name) values("白茶");
insert into productType(name) values("绿茶");
insert into productType(name) values("红茶");