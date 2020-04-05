/*
 Navicat Premium Data Transfer

 Source Server         : vm
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : 192.168.1.10:3306
 Source Schema         : tea_club

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 04/04/2020 23:12:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authorityEnd
-- ----------------------------
DROP TABLE IF EXISTS `authorityEnd`;
CREATE TABLE `authorityEnd`  (
  `uid` int(0) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后端路由',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `belongFront` int(0) NULL DEFAULT NULL COMMENT '对应发出请求的前端页面uid',
  `method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `belongFront`(`belongFront`) USING BTREE,
  CONSTRAINT `authorityEnd_ibfk_1` FOREIGN KEY (`belongFront`) REFERENCES `authorityFront` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后端路由表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
