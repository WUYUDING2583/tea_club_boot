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

 Date: 04/04/2020 23:09:18
 Description: 职员后端路由权限表
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for positionAuthorityEndDetail
-- ----------------------------
DROP TABLE IF EXISTS `positionAuthorityEndDetail`;
CREATE TABLE `positionAuthorityEndDetail`  (
  `uid` int(0) NOT NULL AUTO_INCREMENT,
  `positionId` int(0) NULL DEFAULT NULL,
  `authorityEndId` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `authorityEndId`(`authorityEndId`) USING BTREE,
  INDEX `positionId`(`positionId`) USING BTREE,
  CONSTRAINT `positionAuthorityEndDetail_ibfk_1` FOREIGN KEY (`authorityEndId`) REFERENCES `authorityEnd` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `positionAuthorityEndDetail_ibfk_2` FOREIGN KEY (`positionId`) REFERENCES `position` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
