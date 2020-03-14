DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
                         `uid` int(11) NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) DEFAULT NULL,
                         `address` varchar(255) DEFAULT NULL,
                         `description` varchar(255) DEFAULT NULL,
                         `contact` varchar(20) DEFAULT NULL,
                         PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
insert into shop(name) values("西湖店");
insert into shop(name) values("下沙店");

