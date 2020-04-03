DROP TABLE IF EXISTS `authorityDetail`;
CREATE TABLE `authorityDetail` (
                           `uid` int(11) NOT NULL AUTO_INCREMENT,
                           `name` varchar(100) DEFAULT  NULL,
                           `title` varchar(100) DEFAULT  NULL,
                           `component` varchar(100) DEFAULT  NULL,
                           `description` varchar(200) DEFAULT  NULL,
                           `belong` int(11) default null,
                           FOREIGN KEY (`belong`) REFERENCES authority(uid) ON DELETE CASCADE ON UPDATE CASCADE,
                           PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
insert into authorityDetail(name,title,component,description,belong) values("company_info","公司信息管理","Admin/CompanyInfo","这是公司信息管理",1);
insert into authorityDetail(name,title,component,description,belong) values("shops","门店管理","Admin/ShopManagement","这是门店管理",2);
insert into authorityDetail(name,title,component,description,belong) values("add_shop","新增门店","Admin/AddShop","这是新增门店",2);
insert into authorityDetail(name,title,component,description,belong) values("add_shop_box","新增包厢","Admin/AddShopBox","这是新增包厢",2);
insert into authorityDetail(name,title,component,description,belong) values("shop_boxes","包厢管理","Admin/ShopBoxManagement","这是包厢管理",2);
insert into authorityDetail(name,title,component,description,belong) values("clerks","职员管理","Admin/ClerkManagement","这是人员信息管理",3);
insert into authorityDetail(name,title,component,description,belong) values("add_clerk","新增职员","Admin/AddClerk","这是新增人员",3);
insert into authorityDetail(name,title,component,description,belong) values("add_activity","新增活动","Admin/AddActivity","这是新增活动",4);
insert into authorityDetail(name,title,component,description,belong) values("activities","活动管理","Admin/ActivityManagement","这是活动管理",4);
insert into authorityDetail(name,title,component,description,belong) values("add_product","新增产品","Admin/AddProduct","这是新增产品",5);
insert into authorityDetail(name,title,component,description,belong) values("products","产品管理","Admin/ProductManagement","这是产品管理",5);
insert into authorityDetail(name,title,component,description,belong) values("enterprise_customer_applications","企业用户申请","Admin/EnterpriseCustomerApplication","这是企业用户申请",6);
insert into authorityDetail(name,title,component,description,belong) values("customers","客户信息","Admin/CustomerManagement","这是客户信息",6);
insert into authorityDetail(name,title,component,description,belong) values("orders","订单信息","Admin/OrderManagement","这是订单信息",7);
insert into authorityDetail(name,title,component,description,belong) values("add_article","新增文章","Admin/AddArticle","这是新增文章",8);
insert into authorityDetail(name,title,component,description,belong) values("articles","文章管理","Admin/ArticleManagement","这是文章管理",8);
insert into authorityDetail(name,title,component,description,belong) values("test","test","test","这是文章管理",8);