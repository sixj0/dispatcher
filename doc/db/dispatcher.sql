CREATE TABLE `dispatcher` (
  `code` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `request_method` varchar(255) NOT NULL COMMENT '请求方式',
  `request_url` varchar(255) NOT NULL COMMENT '请求地址',
  `request_headers` varchar(255) DEFAULT NULL COMMENT '其他系统所需要的请求头，多个用逗号隔开',
  `data` varchar(64) DEFAULT NULL COMMENT '返回值数据结构对应关系（data）',
  `status` varchar(64) DEFAULT NULL COMMENT '返回值数据结构对应关系（status）',
  `status_ok` varchar(64) DEFAULT NULL COMMENT '成功状态码',
  `message` varchar(64) DEFAULT NULL COMMENT '返回值数据结构对应关系（message）',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='请求转发';
