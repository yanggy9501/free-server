CREATE TABLE `oauth_client_details` (
    `client_id` varchar(256) CHARACTER SET utf8 NOT NULL COMMENT '客服端ID',
    `resource_ids` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '客服端资源列表',
    `client_secret` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '客服端密钥',
    `scope` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '授权范围',
    `authorized_grant_types` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '授权类型',
    `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '回调地址',
    `authorities` varchar(256) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限',
    `access_token_validity` int(11) DEFAULT NULL COMMENT '访问令牌有效时间',
    `refresh_token_validity` int(11) DEFAULT NULL COMMENT '刷新令牌有效时间',
    `additional_information` varchar(4096) CHARACTER SET utf8 DEFAULT NULL COMMENT '额外信息',
    `autoapprove` varchar(256) CHARACTER SET utf8 DEFAULT NULL,
    `gmt_create` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
    `gmt_modified` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;