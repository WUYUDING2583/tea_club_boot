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

 Date: 04/04/2020 23:07:59
 Description: 职员前端页面权限表
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for positionAuthorityFrontDetail
-- ----------------------------
DROP TABLE IF EXISTS `positionAuthorityFrontDetail`;
CREATE TABLE `positionAuthorityFrontDetail`  (
  `uid` int(0) NOT NULL AUTO_INCREMENT,
  `positionId` int(0) NULL DEFAULT NULL,
  `authorityFrontId` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `authorityFrontId`(`authorityFrontId`) USING BTREE,
  INDEX `positionId`(`positionId`) USING BTREE,
  CONSTRAINT `positionAuthorityFrontDetail_ibfk_1` FOREIGN KEY (`authorityFrontId`) REFERENCES `authorityFront` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `positionAuthorityFrontDetail_ibfk_2` FOREIGN KEY (`positionId`) REFERENCES `position` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
