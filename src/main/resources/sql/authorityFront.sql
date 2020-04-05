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

 Date: 04/04/2020 23:20:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authorityFront
-- ----------------------------
DROP TABLE IF EXISTS `authorityFront`;
CREATE TABLE `authorityFront`  (
  `uid` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页面对应的路由名称',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页面名称',
  `component` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页面对应组件路径以src/container为根目录',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页面描述',
  `belong` int(0) NULL DEFAULT NULL COMMENT '页面所属大类uid',
  `auth` tinyint(0) NULL DEFAULT 1 COMMENT '是否需要权限校验',
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `belong`(`belong`) USING BTREE,
  CONSTRAINT `authorityFront_ibfk_1` FOREIGN KEY (`belong`) REFERENCES `authority` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '前端页面路由表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authorityFront
-- ----------------------------
INSERT INTO `authorityFront` VALUES (1, 'company_info', '公司信息管理', 'Admin/CompanyInfo', '这是公司信息管理', 1, 1);
INSERT INTO `authorityFront` VALUES (2, 'shops', '门店管理', 'Admin/ShopManagement', '这是门店管理', 2, 1);
INSERT INTO `authorityFront` VALUES (3, 'add_shop', '新增门店', 'Admin/AddShop', '这是新增门店', 2, 1);
INSERT INTO `authorityFront` VALUES (4, 'add_shop_box', '新增包厢', 'Admin/AddShopBox', '这是新增包厢', 2, 1);
INSERT INTO `authorityFront` VALUES (5, 'shop_boxes', '包厢管理', 'Admin/ShopBoxManagement', '这是包厢管理', 2, 1);
INSERT INTO `authorityFront` VALUES (6, 'clerks', '职员管理', 'Admin/ClerkManagement', '这是人员信息管理', 3, 1);
INSERT INTO `authorityFront` VALUES (7, 'add_clerk', '新增职员', 'Admin/AddClerk', '这是新增人员', 3, 1);
INSERT INTO `authorityFront` VALUES (8, 'add_activity', '新增活动', 'Admin/AddActivity', '这是新增活动', 4, 1);
INSERT INTO `authorityFront` VALUES (9, 'activities', '活动管理', 'Admin/ActivityManagement', '这是活动管理', 4, 1);
INSERT INTO `authorityFront` VALUES (10, 'add_product', '新增产品', 'Admin/AddProduct', '这是新增产品', 5, 1);
INSERT INTO `authorityFront` VALUES (11, 'products', '产品管理', 'Admin/ProductManagement', '这是产品管理', 5, 1);
INSERT INTO `authorityFront` VALUES (12, 'enterprise_customer_applications', '企业用户申请', 'Admin/EnterpriseCustomerApplication', '这是企业用户申请', 6, 1);
INSERT INTO `authorityFront` VALUES (13, 'customers', '客户信息', 'Admin/CustomerManagement', '这是客户信息', 6, 1);
INSERT INTO `authorityFront` VALUES (14, 'orders', '订单信息', 'Admin/OrderManagement', '这是订单信息', 7, 1);
INSERT INTO `authorityFront` VALUES (15, 'add_article', '新增文章', 'Admin/AddArticle', '这是新增文章', 8, 1);
INSERT INTO `authorityFront` VALUES (16, 'articles', '文章管理', 'Admin/ArticleManagement', '这是文章管理', 8, 1);
INSERT INTO `authorityFront` VALUES (17, 'test', 'test', 'test', '这是文章管理', 8, 1);

SET FOREIGN_KEY_CHECKS = 1;
