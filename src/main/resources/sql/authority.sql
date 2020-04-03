DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
                            `uid` int(11) NOT NULL AUTO_INCREMENT,
                            `name` varchar(100) DEFAULT  NULL,
                            `title` varchar(100) DEFAULT  NULL,
                            `icon` varchar(100) DEFAULT  NULL,
                            PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
insert into authority(name,title,icon) values("company_management","公司管理","book");
insert into authority(name,title,icon) values("shop_management","门店管理","shop");
insert into authority(name,title,icon) values("clerk_management","职员管理","user");
insert into authority(name,title,icon) values("activity_management","活动管理","calendar");
insert into authority(name,title,icon) values("product_management","产品管理","project");
insert into authority(name,title,icon) values("customer_management","客户管理","customer-service");
insert into authority(name,title,icon) values("order_management","订单管理","snippets");
insert into authority(name,title,icon) values("article_management","文章管理","container");
