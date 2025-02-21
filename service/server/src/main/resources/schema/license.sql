CREATE TABLE IF NOT EXISTS `license_server`.`license`
(
    `id`                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `product_id`        BIGINT UNSIGNED NOT NULL COMMENT '产品 id',
    `customer_id`       BIGINT UNSIGNED NOT NULL COMMENT '客户 id',
    `license_cipher`    VARCHAR(128)    NOT NULL COMMENT 'license 密文',
    `license_hash`      VARCHAR(64)     NOT NULL COMMENT 'license HASH',
    `license_mask`      VARCHAR(32)     NOT NULL COMMENT 'license 掩码',
    `period_start`      DATE            NOT NULL COMMENT '有效期开始',
    `period_end`        DATE                     DEFAULT NULL COMMENT '有效期结束',
    `long_term`         BOOLEAN         NOT NULL DEFAULT FALSE COMMENT '是否长期有效：FALSE 有限期，TRUE 长期',
    `app_id`            VARCHAR(32)     NOT NULL COMMENT '应用 id',
    `app_key_cipher`    VARCHAR(128)    NOT NULL COMMENT '应用 key 密文',
    `app_key_hash`      VARCHAR(64)     NOT NULL COMMENT '应用 key HASH',
    `app_key_mask`      VARCHAR(32)     NOT NULL COMMENT '应用 key 掩码',
    `app_secret_cipher` VARCHAR(128)    NOT NULL COMMENT '应用 secret 密文',
    `app_secret_hash`   VARCHAR(64)     NOT NULL COMMENT '应用 secret HASH',
    `app_secret_mask`   VARCHAR(32)     NOT NULL COMMENT '应用 secret 掩码',
    `create_time`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `valid`             BOOLEAN         NOT NULL DEFAULT TRUE COMMENT '是否有效：FALSE 无效，TRUE 有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY `product_id_customer_id` (`product_id`, `customer_id`),
    UNIQUE KEY `license_cipher` (`license_cipher`),
    UNIQUE KEY `license_hash` (`license_hash`),
    UNIQUE KEY `app_id` (`app_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='@DaoDao 许可证';
