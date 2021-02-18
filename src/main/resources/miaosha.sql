/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : miaosha

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 18/02/2021 22:33:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `count` int DEFAULT NULL,
  `sale` int DEFAULT NULL,
  `version` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of stock
-- ----------------------------
BEGIN;
INSERT INTO `stock` VALUES (1, 'mate40', 100, 11, 11);
COMMIT;

-- ----------------------------
-- Table structure for stock_order
-- ----------------------------
DROP TABLE IF EXISTS `stock_order`;
CREATE TABLE `stock_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sid` int DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1665 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of stock_order
-- ----------------------------
BEGIN;
INSERT INTO `stock_order` VALUES (1654, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1655, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1656, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1657, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1658, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1659, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1660, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1661, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1662, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1663, 1, 'mate40', '2021-02-18 08:18:58');
INSERT INTO `stock_order` VALUES (1664, 1, 'mate40', '2021-02-18 08:18:58');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'tak', '123');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
