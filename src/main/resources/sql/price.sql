DROP TABLE IF EXISTS `price`;
CREATE TABLE `price` (
                      `uid` int(11) NOT NULL AUTO_INCREMENT,
                      `ingot` float default null,
                      `credit` float default null,
                      PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;