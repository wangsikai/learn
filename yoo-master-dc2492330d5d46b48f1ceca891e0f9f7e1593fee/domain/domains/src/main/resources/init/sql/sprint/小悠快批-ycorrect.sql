-- 新建批改习题表
CREATE TABLE `correct_question` (
  `id` bigint(20) NOT NULL,
  `question_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  `question_type` tinyint(4) NOT NULL,
  `question_source` tinyint(4) NOT NULL,
  `question_category` tinyint(4) NOT NULL,
  `biz_id` bigint(20) NOT NULL,
  `weight` decimal(5,2) NOT NULL,
  `create_at` datetime(3) NOT NULL,
  `right_rate` tinyint(4) DEFAULT NULL,
  `correct_user_id` bigint(20) DEFAULT NULL,
  `correct_user_ids` varchar(4000) DEFAULT NULL,
  `first_allot_at` datetime(3) DEFAULT NULL,
  `allot_at` datetime(3) DEFAULT NULL,
  `complete_at` datetime(3) DEFAULT NULL,
  `cost_time` int(10) DEFAULT '0',
  `fee` decimal(10,2) DEFAULT NULL,
  `reduce_fee` decimal(10,2) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `is_error` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `idx_question_id` (`question_id`) USING BTREE,
  KEY `idx_biz_id` (`biz_id`) USING BTREE,
  KEY `idx_correct_user_id` (`correct_user_id`) USING BTREE
);

-- 新建批改账目流水表
CREATE TABLE `correct_bills` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `bill_type` tinyint(4) NOT NULL,
  `amount` decimal(10,2) DEFAULT '0.00',
  `biz_id` bigint(20) NOT NULL,
  `memo` varchar(200) DEFAULT '',
  `create_at` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_create_at` (`create_at`)
);

-- 新建批改配置表
CREATE TABLE `correct_config` (
  `id` bigint(20) NOT NULL,
  `fee_cfg` varchar(200) NOT NULL,
  `reward_cfg` varchar(2000) DEFAULT NULL,
  `category_weight_cfgs` varchar(500) DEFAULT NULL,
  `source_weight_cfgs` varchar(500) DEFAULT NULL,
  `default_trust_rank` int(10) NOT NULL,
  `min_trust_rank` int(10) DEFAULT '0',
  `withdraw_bt` datetime(3) DEFAULT NULL,
  `withdraw_et` datetime(3) DEFAULT NULL,
  `day_withdraw_max` decimal(10,2) DEFAULT NULL,
  `withdraw_min` decimal(10,2) DEFAULT '0.00',
  `day_withdraw_count` tinyint(4) DEFAULT '1',
  `transfer_time_comment` varchar(100) DEFAULT '',
  `create_id` bigint(20) DEFAULT NULL,
  `create_at` datetime(3) NOT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `withdraw_week_day` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- 新建快批日统计数据
CREATE TABLE `correct_day_stat` (
  `id` bigint(20) NOT NULL,
  `stat_at` date NOT NULL,
  `allot_count` int(10) DEFAULT '0',
  `blank_question_count` int(10) DEFAULT '0',
  `answer_question_count` int(10) DEFAULT '0',
  `allot_blank_question_avg_time` bigint(20) DEFAULT '0',
  `allot_answer_question_avg_time` bigint(20) DEFAULT '0',
  `blank_question_time` int(10) DEFAULT '0',
  `answer_question_time` int(10) DEFAULT '0',
  `blank_question_error_count` int(10) DEFAULT '0',
  `answer_question_error_count` int(10) DEFAULT '0',
  `create_at` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_month_date` (`stat_at`) USING BTREE
);

-- 新建快批月统计
CREATE TABLE `correct_month_stat` (
  `id` bigint(20) NOT NULL,
  `month_date` date NOT NULL,
  `allot_count` int(10) DEFAULT '0',
  `blank_question_count` int(10) DEFAULT '0',
  `answer_question_count` int(10) DEFAULT '0',
  `category_count` varchar(500) DEFAULT NULL,
  `source_count` varchar(500) DEFAULT NULL,
  `period_count` varchar(500) DEFAULT NULL,
  `create_at` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_month_date` (`month_date`) USING BTREE
);

-- 新建快批用户表
CREATE TABLE `correct_user` (
  `id` bigint(20) NOT NULL,
  `user_type` tinyint(4) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `avatar_id` bigint(20) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `phase_id` tinyint(4) DEFAULT NULL,
  `school_id` bigint(20) DEFAULT NULL,
  `qualification_auth_status` tinyint(4) DEFAULT '0',
  `qualification_imgs` varchar(128) DEFAULT NULL,
  `id_card_auth_status` tinyint(4) DEFAULT '0',
  `id_card` varchar(30) DEFAULT '',
  `real_name` varchar(20) DEFAULT '',
  `card_imgs` varchar(128) DEFAULT '',
  `trust_rank` int(10) DEFAULT '0',
  `balance` decimal(10,2) DEFAULT '0.00',
  `create_at` datetime(3) NOT NULL,
  `source` tinyint(4) NOT NULL,
  `alipay_no` varchar(128) DEFAULT '',
  `status` tinyint(4) NOT NULL,
  `is_pass_simulation` tinyint(1) DEFAULT '0',
  `account_name` varchar(40) NOT NULL,
  `school_name` varchar(128) DEFAULT NULL,
  `teacher_qualification_no_pass_r` varchar(1000) DEFAULT NULL,
  `id_card_no_pass_r` varchar(1000) DEFAULT NULL,
  `auth_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_account_id` (`user_id`) USING BTREE
);

-- 新建快批用户月度统计数据
CREATE TABLE `correct_user_month_stat` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `month_date` date NOT NULL,
  `blank_question_count` int(10) DEFAULT '0',
  `blank_question_error_count` int(10) DEFAULT '0',
  `blank_question_cost_time` int(10) DEFAULT '0',
  `answer_question_count` int(10) DEFAULT '0',
  `answer_question_error_count` int(10) DEFAULT '0',
  `answer_question_cost_time` int(10) DEFAULT '0',
  `blank_question_correct_fee` decimal(10,2) DEFAULT '0.00',
  `answer_question_correct_fee` decimal(10,2) DEFAULT '0.00',
  `reward_fee` decimal(10,2) DEFAULT '0.00',
  `reduce_fee` decimal(10,2) DEFAULT '0.00',
  `create_at` datetime(3) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
);

-- 用户总体统计数据
CREATE TABLE `correct_user_stat` (
  `user_id` bigint(20) NOT NULL,
  `blank_question_time` int(10) DEFAULT '0',
  `answer_question_time` int(10) DEFAULT '0',
  `blank_question_error_rate` decimal(10,2) DEFAULT '0.00',
  `answer_question_error_rate` decimal(10,2) DEFAULT '0.00',
  `allot_question_count` int(10) DEFAULT '0',
  `correct_count` int(10) DEFAULT '0',
  `create_at` datetime(3) NOT NULL,
  `update_at` datetime(3) NOT NULL,
  `first_correct_count` int(10) DEFAULT '0',
  PRIMARY KEY (`user_id`)
);

-- 提现申请表
CREATE TABLE `correct_withdraw_apply` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `alipay_no` varchar(128) NOT NULL,
  `real_name` varchar(20) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `memo` varchar(200)  DEFAULT '',
  `create_at` datetime(3) NOT NULL,
  `alipay_order_id` varchar(128) NOT NULL,
  `fail_memo` varchar(500) DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
);

-- 批改用户池表
CREATE TABLE `correct_user_pool` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `correct_question_id` bigint(20) DEFAULT NULL,
  `question_type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `start_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
);

-- 预置小悠快批config配置数据
INSERT INTO `correct_config` (`id`, `fee_cfg`, `reward_cfg`, `category_weight_cfgs`, `source_weight_cfgs`, `default_trust_rank`, `min_trust_rank`, `withdraw_bt`, `withdraw_et`, `day_withdraw_max`, `withdraw_min`, `day_withdraw_count`, `transfer_time_comment`, `create_id`, `create_at`, `status`, `withdraw_week_day`) 
VALUES ('939901521066205184', '{\"answerQuestionFee\":0.2,\"answerQuestionReduceRate\":3,\"blankQuestionFee\":0.05,\"blankQuestionReduceRate\":3}', '{\"dayCorrectRewards\":[{\"fee\":5,\"type\":\"DAY\",\"createAt\":1521785733597,\"correctQuestionCount\":200},{\"fee\":10,\"type\":\"DAY\",\"createAt\":1521785743078,\"correctQuestionCount\":500},{\"fee\":20,\"type\":\"DAY\",\"createAt\":1521785754550,\"correctQuestionCount\":800}]}', '[{\"category\":\"BLANK_QUESTION_UNKNOW\",\"weight\":1.0},{\"category\":\"ANSWER_QUESTION\",\"weight\":0.9},{\"category\":\"BLANK_QUESTION_APPEAL\",\"weight\":0.8},{\"category\":\"ANSWER_QUESTION_APPEAL\",\"weight\":1.0}]', '[{\"source\":\"HOMEWORK\",\"weight\":1.0},{\"source\":\"AMEND\",\"weight\":0.9},{\"source\":\"PRACTICE\",\"weight\":0.8}]', '80', '60', '2018-03-17 09:00:00.000', '2018-03-17 22:00:00.000', '800.00', '1.00', '1', '24小时内到账', '111111', '2018-03-12 16:56:32.553', '0', '5');

-- 批改员信任值更新统计记录表
CREATE TABLE `trustrank_stat_log` (
  `id` bigint(20) NOT NULL,
  `correct_user_id` bigint(20) DEFAULT NULL,
  `query_offset` int(11) NOT NULL DEFAULT '0',
  `total_error_count` int(11) NOT NULL DEFAULT '0',
  `cur_error_count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
);
