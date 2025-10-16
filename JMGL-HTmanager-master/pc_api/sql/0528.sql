-- 数据库通用设置（可根据实际需求修改）
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0; -- 禁用外键检查（避免建表顺序影响）


-- ----------------------------
-- Table structure for questiner
-- ----------------------------
CREATE TABLE IF NOT EXISTS `questiner` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `clientId` INT COMMENT '客户ID（原外键）',
  `sTime` DATETIME COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提问者表';


-- ----------------------------
-- Table structure for client
-- ----------------------------
CREATE TABLE IF NOT EXISTS `client` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `phone` CHAR(11) COMMENT '电话号码',
  `idNum` CHAR(18) COMMENT '身份证号',
  `clientLevel` TINYINT COMMENT '客户等级（1=普通，2=VIP等）',
  `clientStatus` TINYINT DEFAULT 0 COMMENT '客户状态（0=正常，1=冻结，2=删除）',
  `guideId` INT COMMENT '导购ID（原外键）',
  `sTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `upTime` DATETIME COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户信息表';


-- ----------------------------
-- Table structure for clientEvent
-- ----------------------------
CREATE TABLE IF NOT EXISTS `clientEvent` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `clientId` INT NOT NULL COMMENT '客户ID（原外键）',
  `eType` TINYINT NOT NULL COMMENT '事件类型（1=咨询，2=投诉，3=跟进等）',
  `eTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '事件时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户事件表';


-- ----------------------------
-- Table structure for file
-- ----------------------------
CREATE TABLE IF NOT EXISTS `file` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `fileItem` VARCHAR(50) NOT NULL COMMENT '文件类型（如设计图、合同等）',
  `itemId` INT COMMENT '关联项目ID',
  `isUse` TINYINT DEFAULT 1 COMMENT '是否使用（1=启用，0=禁用）',
  `desc` TEXT COMMENT '文件描述',
  `url` VARCHAR(255) NOT NULL COMMENT '文件链接',
  `sTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件存储表';


-- ----------------------------
-- Table structure for sop
-- ----------------------------
CREATE TABLE IF NOT EXISTS `shop` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '店铺名称',
  `address` VARCHAR(255) COMMENT '地址',
  `area` VARCHAR(50) COMMENT '区域',
  `regionId` INT COMMENT '地区ID（原外键）',
  `rent` DECIMAL(10,2) COMMENT '租金',
  `shopDesc` TEXT COMMENT '店铺描述',
  `surroundDesc` TEXT COMMENT '周边描述',
  `trafficDesc` TEXT COMMENT '交通描述',
  `shopStatus` TINYINT DEFAULT 0 COMMENT '店铺状态（0=待出租，1=已出租，2=已关闭）',
  `sTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `upTime` DATETIME COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标准作业流程/店铺信息表';


-- ----------------------------
-- Table structure for buildDetail
-- ----------------------------
CREATE TABLE IF NOT EXISTS `buildDetail` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `taskId` INT NOT NULL COMMENT '任务ID（原外键）',
  `versionId` INT COMMENT '版本ID（原外键）',
  `buildId` INT COMMENT '建设ID（原外键）',
  `buildType` TINYINT COMMENT '建设类型（1=装修，2=改造，3=新建等）',
  `buildDesc` TEXT COMMENT '建设描述',
  `buildFile` INT COMMENT '关联文件ID（原外键，引用file表id）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='建设详情表';


-- ----------------------------
-- Table structure for invite
-- ----------------------------
CREATE TABLE IF NOT EXISTS `invite` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `clientId` INT NOT NULL COMMENT '客户ID（原外键）',
  `guideId` INT COMMENT '导购ID（原外键）',
  `invitationTime` DATETIME COMMENT '邀请时间',
  `sTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `invitationDesc` TEXT COMMENT '邀请描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邀请记录表';


-- ----------------------------
-- Table structure for task
-- ----------------------------
CREATE TABLE IF NOT EXISTS `task` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `clientId` INT COMMENT '客户ID（原外键）',
  `shopId` INT COMMENT '店铺ID（原外键，引用sop表id）',
  `taskStatus` TINYINT DEFAULT 0 COMMENT '任务状态（0=待处理，1=进行中，2=已完成）',
  `sTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `upTime` DATETIME COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务管理表';


-- ----------------------------
-- Table structure for designVersion
-- ----------------------------
CREATE TABLE IF NOT EXISTS `designVersion` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `taskId` INT NOT NULL COMMENT '任务ID（原外键）',
  `versionId` INT COMMENT '版本号',
  `desFileId` INT COMMENT '设计文件ID（原外键，引用file表id）',
  `versionDesc` TEXT COMMENT '版本描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设计版本表';


-- ----------------------------
-- Table structure for design
-- ----------------------------
CREATE TABLE `drawings` (
                            `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '图纸主表ID',
                            `designId` BIGINT NOT NULL COMMENT '设计id',
                            `shopId` BIGINT NOT NULL COMMENT '商铺编码',
                            `drawingType` BIGINT NOT NULL COMMENT '图纸类型，0封面，1目录，2施工设计说明，3室内装修材料，4材料表，5平面图，6立面图，7大图样',
                            `pageSize` VARCHAR(10) COMMENT '图幅',
                            `drawingNo` VARCHAR(50) NOT NULL COMMENT '图纸编号（如 P-01、E-02 ）',
                            `drawingName` VARCHAR(255) NOT NULL COMMENT '图纸名称（如 平面布置图 ）',
                            `fileUrl` VARCHAR(50) COMMENT '文件地址',
                            `creator` VARCHAR(50) COMMENT '创建人',
                            `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `upTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `versionId` BIGINT UNSIGNED COMMENT '关联最新版本ID（关联 drawing_versions.id ）',
                            `isActive` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效（1-有效，0-作废）',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图纸信息';

CREATE TABLE IF NOT EXISTS `design` (
                                        `id` INT AUTO_INCREMENT COMMENT '主键ID',
                                        `shopId` INT COMMENT '店铺ID（原外键，引用sop表id）',
                                        `clientId` INT COMMENT '客户ID（原外键）',
                                        `designStatus` TINYINT DEFAULT 0 COMMENT '设计状态（0=待设计，1=设计中，2=已完成）',
                                        `designType` TINYINT DEFAULT 0 COMMENT '设计类型（目前不知道什么意思）',
                                        `designerId` INT COMMENT '设计师姓名',
                                        `sTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `upTime` DATETIME COMMENT '更新时间',
                                        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设计管理表';

CREATE TABLE `measure` (
                           `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '测量ID',
                           `designId` BIGINT NOT NULL COMMENT '设计id',
                           `measureType` BIGINT NOT NULL COMMENT '测量来源',
                           `fileUrl` VARCHAR(50) COMMENT '文件地址',
                           `creator` VARCHAR(50) COMMENT '创建人',
                           `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `upTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测量信息';


-- ----------------------------
-- Table structure for build
-- ----------------------------
CREATE TABLE IF NOT EXISTS `build` (
  `id` INT AUTO_INCREMENT COMMENT '主键ID',
  `taskId` INT NOT NULL COMMENT '任务ID（原外键）',
  `shopId` INT COMMENT '店铺ID（原外键，引用sop表id）',
  `clientId` INT COMMENT '客户ID（原外键）',
  `sTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `upTime` DATETIME COMMENT '更新时间',
  `buyStatus` TINYINT DEFAULT 0 COMMENT '采购状态（0=未采购，1=采购中，2=已完成）',
  `superviseStatus` TINYINT DEFAULT 0 COMMENT '监理状态（0=未开始，1=进行中，2=已完成）',
  `acceptanceStatus` TINYINT DEFAULT 0 COMMENT '验收状态（0=未验收，1=验收中，2=已通过）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='建设管理表';



CREATE TABLE `franchise_investment_form` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '电话号码',
  `province` VARCHAR(20) NOT NULL COMMENT '省份',
  `residence` VARCHAR(100) NOT NULL COMMENT '居住地',
  `frequent_store` VARCHAR(100) DEFAULT NULL COMMENT '经常光顾店',
  `store_type` VARCHAR(20) NOT NULL COMMENT '门店形式（直营店/加盟店/联营店/经销商/其他）',
  `remarks` TEXT DEFAULT NULL COMMENT '备注',
  `investment_budget` VARCHAR(20) NOT NULL COMMENT '投资预算（5万以下/5-10万/10-20万/20-50万/50-100万/100万以上）',
  `cooperation_mode` VARCHAR(20) NOT NULL COMMENT '合作模式（单店加盟/区域代理/合伙经营/技术合作/其他）',
  `expected_store_address` VARCHAR(255) DEFAULT NULL COMMENT '期望开店地址',
  `expected_province` VARCHAR(20) DEFAULT NULL COMMENT '期望开店省份',
  `expected_city` VARCHAR(20) DEFAULT NULL COMMENT '期望开店市',
  `expected_district` VARCHAR(20) DEFAULT NULL COMMENT '期望开店区',
  `industry` VARCHAR(20) NOT NULL COMMENT '从事行业（餐饮/零售/服务/教育/娱乐/其他）',
  `development_opinion` TEXT NOT NULL COMMENT '如何看待发展',
  `selection_reason` TEXT NOT NULL COMMENT '选择我的原因',
  `improvement_suggestion` TEXT DEFAULT NULL COMMENT '我们提升的建议',
  `business_district` VARCHAR(20) DEFAULT NULL COMMENT '你认为的商圈（核心商圈/区域商圈/社区商圈/交通枢纽商圈/旅游商圈/其他）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_phone` (`phone`),
  KEY `idx_province` (`province`),
  KEY `idx_industry` (`industry`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='加盟投资意向表';


SET FOREIGN_KEY_CHECKS = 1; -- 恢复外键检查

CREATE TABLE `business_district` (
                                     `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商圈ID',
                                     `name` VARCHAR(100) NOT NULL COMMENT '商圈名称',
                                     `address` VARCHAR(255) NOT NULL COMMENT '商圈地址',
                                     `region` VARCHAR(50) NOT NULL COMMENT '所属区域',
                                     `customer_id` BIGINT NOT NULL COMMENT '关联客户ID',
                                     `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `creator_id` BIGINT NOT NULL COMMENT '创建人ID',
                                     PRIMARY KEY (`id`),
                                     KEY `idx_customer` (`customer_id`),
                                     KEY `idx_region` (`region`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='商圈信息表';



CREATE TABLE `note` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '笔记ID',
                        `business_district_id` BIGINT NOT NULL COMMENT '关联商圈ID',
                        `follower_id` BIGINT NOT NULL COMMENT '跟进人员ID',
                        `description` TEXT NOT NULL COMMENT '文字描述',
                        `image_urls` TEXT COMMENT '图片URL集合（逗号分隔）',
                        `video_url` VARCHAR(255) COMMENT '视频URL',
                        `checkin_location` VARCHAR(255) NOT NULL COMMENT '打卡位置（经纬度+地址）',
                        `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='商圈跟进笔记表（含打卡）';



CREATE TABLE `districtfollower` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                    `district_id` BIGINT NOT NULL COMMENT '关联商圈ID',
                                    `follower_id` BIGINT NOT NULL COMMENT '关联跟进ID',
                                    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `creator_id` BIGINT NOT NULL COMMENT '创建人ID',
                                    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='商圈跟进人员表';




-- 1. 添加基本信息字段
ALTER TABLE `lease`.`client`
ADD COLUMN `province` varchar(20) NULL DEFAULT NULL COMMENT '省份' AFTER `oldHouse`,
ADD COLUMN `residence` varchar(100) NULL DEFAULT NULL COMMENT '居住地' AFTER `province`;

-- 2. 添加投资意向核心字段
ALTER TABLE `lease`.`client`
ADD COLUMN `business_category` varchar(50) NULL DEFAULT NULL COMMENT '经营品类' AFTER `residence`,
ADD COLUMN `frequent_store` varchar(100) NULL DEFAULT NULL COMMENT '经常光顾店' AFTER `business_category`,
ADD COLUMN `store_type` varchar(20) NULL DEFAULT NULL COMMENT '门店形式' AFTER `frequent_store`,
ADD COLUMN `investment_budget` varchar(20) NULL DEFAULT NULL COMMENT '投资预算' AFTER `store_type`,
ADD COLUMN `cooperation_mode` varchar(20) NULL DEFAULT NULL COMMENT '合作模式（单店加盟/区域代理/合伙经营/技术合作/其他）' AFTER `investment_budget`,
ADD COLUMN `expected_store_address` varchar(255) NULL DEFAULT NULL COMMENT '期望开店地址' AFTER `cooperation_mode`,
ADD COLUMN `expected_province` varchar(20) NULL DEFAULT NULL COMMENT '期望开店省份' AFTER `expected_store_address`,
ADD COLUMN `expected_city` varchar(20) NULL DEFAULT NULL COMMENT '期望开店市' AFTER `expected_province`,
ADD COLUMN `expected_district` varchar(100) NULL DEFAULT NULL COMMENT '期望开店区' AFTER `expected_city`;

-- 3. 添加业务分类与建议字段
ALTER TABLE `lease`.`client`
ADD COLUMN `industry` varchar(20) NULL DEFAULT NULL COMMENT '从事行业（餐饮/零售/服务/教育/娱乐/其他）' AFTER `expected_district`,
ADD COLUMN `business_district` varchar(200) NULL DEFAULT NULL COMMENT '你认为的商圈（核心商圈/区域商圈/社区商圈/交通枢纽商圈/旅游商圈/其他）' AFTER `industry`,
ADD COLUMN `remarks` text NULL COMMENT '备注' AFTER `business_district`,
ADD COLUMN `development_opinion` text NULL COMMENT '如何看待发展' AFTER `remarks`,
ADD COLUMN `selection_reason` text NULL COMMENT '选择我的原因' AFTER `development_opinion`,
ADD COLUMN `improvement_suggestion` text NULL COMMENT '我们提升的建议' AFTER `selection_reason`;



