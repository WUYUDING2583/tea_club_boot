DROP TABLE IF EXISTS `enterpriseCustomerApplication`;
CREATE TABLE `enterpriseCustomerApplication` (
                            `uid` int(11) NOT NULL AUTO_INCREMENT,
                            `applyTime` bigint default null,
                            `enterpriseId` int(11) default null,
                            `applicantId` int(11) default null,
                            `status` varchar(10) default null,
                            FOREIGN KEY (`enterpriseId`) REFERENCES enterprise(uid),
                            FOREIGN KEY (`applicantId`) REFERENCES customer(uid),
                            PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
