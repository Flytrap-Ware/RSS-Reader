CREATE TABLE IF NOT EXISTS `account`
(
    `id`           bigint                   NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`         varchar(255)             NOT NULL,
    `email`        varchar(255)             NOT NULL,
    `profile`      varchar(2500)            NOT NULL,
    `oauth_pk`     bigint                   NOT NULL,
    `oauth_server` enum ('GITHUB')          NOT NULL,
    `roll`         enum('ADMIN','GENERAL')  NOT NULL,
    `created_at`   date                     NOT NULL
);

CREATE TABLE IF NOT EXISTS `rss_source`
(
    `id`                bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `title`             varchar(2500),
    `url`               varchar(2500) NOT NULL,
    `platform`          varchar(25),
    `last_collected_at` datetime(6),
    `fail_count`        datetime(6),
    `restriction_until` datetime(6)
);

CREATE TABLE IF NOT EXISTS `post`
(
    `id`            varchar(255)        NOT NULL PRIMARY KEY,
    `guid`          varchar(2500) NOT NULL,
    `rss_source_id` bigint        NOT NULL,
    `title`         varchar(2500) NOT NULL,
    `thumbnail_url` varchar(2500),
    `description`   longtext      NOT NULL,
    `pub_date`      timestamp     NOT NULL
);

CREATE TABLE IF NOT EXISTS `open`
(
    `id`           bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `post_id`      varchar(255)        NOT NULL,
    `account_id`   bigint        NOT NULL
);

CREATE TABLE IF NOT EXISTS `folder`
(
    `id`         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name`       varchar(255) NOT NULL,
    `account_id` bigint       NOT NULL,
    `is_shared`  tinyint(1)   NOT NULL DEFAULT 0,
    `is_deleted` tinyint(1)   NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `subscription`
(
    `id`                bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `rss_source_id`     bigint      NOT NULL,
    `folder_id`         bigint      NOT NULL
 );

CREATE TABLE IF NOT EXISTS `shared_member`
(
    `id`            bigint  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `folder_id`     bigint  NOT NULL,
    `account_id`    bigint  NOT NULL
 );

CREATE TABLE IF NOT EXISTS `bookmark`
(
    `id`	        bigint	NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `account_id`	bigint	NOT NULL,
    `post_id` 	    varchar(255)	NOT NULL
);

CREATE TABLE IF NOT EXISTS `alert`
(
    `id`	            bigint	                    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `account_id`	    bigint	                    NOT NULL,
    `folder_id`	        bigint	                    NOT NULL,
    `webhook_url`       varchar(2500)               NOT NULL,
    `alert_platform`    enum('DISCORD', 'SLACK')
);

CREATE TABLE IF NOT EXISTS `admin_system`
(
    `id`                            bigint  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `post_collection_enabled`       tinyint(1)   NOT NULL DEFAULT 0,
    `post_collection_delay`         bigint,
    `post_collection_batch_size`    int,
    `core_thread_pool_size`         int
);

CREATE TABLE IF NOT EXISTS `rss_post_stat`
(
    `id`            bigint          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `platform`	    varchar(25)	    NOT NULL,
    `post_count`    bigint          NOT NULL,
    `pub_date`      timestamp       NOT NULL
);
