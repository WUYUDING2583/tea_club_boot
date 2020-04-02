DROP TABLE IF EXISTS `articleTag`;
CREATE TABLE `articleTag` (
                         `uid` int(11) NOT NULL AUTO_INCREMENT,
                         `articleId` int(11) DEFAULT  NULL,
                         `tagId` int(11) default null,
                         FOREIGN KEY (`articleId`) REFERENCES article(uid)ON DELETE CASCADE ON UPDATE CASCADE,
                         FOREIGN KEY (`tagId`) REFERENCES tag(uid)ON DELETE CASCADE ON UPDATE CASCADE,
                         PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
