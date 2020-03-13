SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `company` (
                            `uid` int(11) NOT NULL AUTO_INCREMENT,
                            `companyName` varchar(255) DEFAULT NULL,
                            `postCode` varchar(10),
                            `contact` varchar(15),
                            `websiteName`varchar(100),
                            `weChatOfficialAccount` varchar(50),
                            `address` varchar(255),
                            PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

