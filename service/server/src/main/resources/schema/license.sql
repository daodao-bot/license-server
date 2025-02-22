CREATE TABLE IF NOT EXISTS `license_server`.`license`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `product_id`     BIGINT UNSIGNED NOT NULL COMMENT '产品 id',
    `customer_id`    BIGINT UNSIGNED NOT NULL COMMENT '客户 id',
    `app_id`         VARCHAR(32)     NOT NULL COMMENT '应用 id',
    `license_cipher` VARCHAR(128)    NOT NULL COMMENT 'License 密文',
    `license_hash`   VARCHAR(64)     NOT NULL COMMENT 'License HASH',
    `license_mask`   VARCHAR(32)     NOT NULL COMMENT 'License 掩码',
    `period_start`   DATE            NOT NULL COMMENT '有效期开始',
    `period_end`     DATE                     DEFAULT NULL COMMENT '有效期结束',
    `long_term`      BOOLEAN         NOT NULL DEFAULT TRUE COMMENT '是否长期有效：FALSE 有限期，TRUE 长期',
    `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `valid`          BOOLEAN         NOT NULL DEFAULT TRUE COMMENT '是否有效：FALSE 无效，TRUE 有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY `product_id_customer_id` (`product_id`, `customer_id`),
    UNIQUE KEY `app_id` (`app_id`),
    KEY `license_cipher` (`license_cipher`),
    KEY `license_hash` (`license_hash`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='@DaoDao 许可证';
