CREATE TABLE IF NOT EXISTS `license_server`.`customer`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name_cipher`  VARCHAR(128)    NOT NULL COMMENT '名称密文',
    `name_hash`    VARCHAR(64)     NOT NULL COMMENT '名称 HASH',
    `name_mask`    VARCHAR(32)     NOT NULL COMMENT '名称掩码',
    `phone_cipher` VARCHAR(64)     NOT NULL COMMENT '手机号密文',
    `phone_hash`   VARCHAR(64)     NOT NULL COMMENT '手机号 HASH',
    `phone_mask`   VARCHAR(32)     NOT NULL COMMENT '手机号掩码',
    `email_cipher` VARCHAR(128)    NOT NULL COMMENT '邮箱密文',
    `email_hash`   VARCHAR(64)     NOT NULL COMMENT '邮箱 HASH',
    `email_mask`   VARCHAR(32)     NOT NULL COMMENT '邮箱掩码',
    `create_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `valid`        BOOLEAN         NOT NULL DEFAULT TRUE COMMENT '是否有效：FALSE 无效，TRUE 有效',
    PRIMARY KEY (`id`),
    KEY `name_cipher` (`name_cipher`),
    KEY `name_hash` (`name_hash`),
    UNIQUE KEY `phone_cipher` (`phone_cipher`),
    UNIQUE KEY `phone_hash` (`phone_hash`),
    UNIQUE KEY `email_cipher` (`email_cipher`),
    UNIQUE KEY `email_hash` (`email_hash`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='@DaoDao 客户';
