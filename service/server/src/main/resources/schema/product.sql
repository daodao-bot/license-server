CREATE TABLE IF NOT EXISTS `license_server`.`product`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `code`        VARCHAR(32)     NOT NULL COMMENT '代码',
    `name`        VARCHAR(64)     NOT NULL COMMENT '名称',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `valid`       BOOLEAN         NOT NULL DEFAULT TRUE COMMENT '是否有效：FALSE 无效，TRUE 有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY `code` (`code`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='@DaoDao 产品';
