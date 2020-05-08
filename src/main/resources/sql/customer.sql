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

 Date: 08/05/2020 20:33:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `uid` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contact` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `identityId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL,
  `gender` int(0) NULL DEFAULT NULL,
  `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `weChatId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ingot` float NULL DEFAULT 0,
  `credit` float NULL DEFAULT 0,
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `type`(`type`) USING BTREE,
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`type`) REFERENCES `customerType` (`uid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (1, 'customer1', 'askfjasdf', 'asdfiusahfa', 'asfs@asdf.com', 3, 0, 'safalss', 'adfuisf', 'aafslf', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
