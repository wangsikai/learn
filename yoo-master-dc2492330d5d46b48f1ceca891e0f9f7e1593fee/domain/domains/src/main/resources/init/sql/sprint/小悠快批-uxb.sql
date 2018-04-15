-- 1、首先把已批改完成的未下发的作业状态改为“已下发”
UPDATE homework hk
INNER JOIN (
 SELECT COUNT(1) AS ac,sh.`homework_id` FROM student_homework sh WHERE sh.`right_rate` IS NOT NULL OR sh.`stu_submit_at` IS NULL
 GROUP BY sh.`homework_id`
) t ON t.homework_id=hk.id AND hk.`distribute_count`=t.ac
SET hk.status=3,hk.all_correct_complete=1
WHERE hk.status=2;

-- 2、再将已下发作业的学生作业状态设为已下发
UPDATE student_homework sh
INNER JOIN homework hk ON hk.id=sh.`homework_id` AND hk.status=3
SET sh.`status`=2
WHERE sh.`status`!=2 AND (sh.`right_rate` IS NOT NULL OR sh.`stu_submit_at` IS NULL);


--  作业中包含未批改的解答题，将解答题全部改为老师自己批改
UPDATE homework hk
INNER JOIN student_homework shk ON hk.id = shk.homework_id 
INNER JOIN student_homework_question shkq ON shk.id = shkq.student_homework_id AND shkq.result IN (0,3)
INNER JOIN question q ON q.id = shkq.question_id AND q.type = 5
SET shkq.correct_type=3,shk.correct_status=3,hk.tobe_corrected=1
WHERE hk.status=2;


-- 添加申述的推送模板
INSERT INTO `uxb`.`push_template` (`code`, `body`, `note`, `title`) VALUES ('12000042', '《${homeworkName}》你申诉的题目已修正批改结果。赠送你20个金币。', '学生申述成功', '悠数学');
INSERT INTO `uxb`.`push_template` (`code`, `body`, `note`, `title`) VALUES ('12000043', '《${homeworkName}》你申诉的题目已被打回，已验证，首次批改结果无误。', '学生申述失败', '悠数学');
INSERT INTO `uxb`.`push_template` (`code`, `body`, `note`, `title`) VALUES ('12000044', '《${homeworkName}》你申诉的${studentName}的题目已修正批改结果。', '老师申述成功', '悠数学');
INSERT INTO `uxb`.`push_template` (`code`, `body`, `note`, `title`) VALUES ('12000045', '《${homeworkName}》你申诉的${studentName}的题目已被打回，已验证，首次批改结果无误。', '老师申述失败', '悠数学');

-- 添加申述的短信模板
INSERT INTO `uxb`.`sms_template` (`code`, `content`, `note`, `body`, `mock`) VALUES ('10000029', NULL, NULL, '【悠学】《${homeworkName}》你申诉的题目已修正批改结果。赠送你20个金币，赶紧打开查看吧。#alidayu#SMS_128115005#alidayu#', b'0');
INSERT INTO `uxb`.`sms_template` (`code`, `content`, `note`, `body`, `mock`) VALUES ('10000030', NULL, NULL, '【悠学】《${homeworkName}》你的申诉已被打回，已验证，首次批改结果无误。#alidayu#SMS_127605029#alidayu#', b'0');
INSERT INTO `uxb`.`sms_template` (`code`, `content`, `note`, `body`, `mock`) VALUES ('10000031', NULL, NULL, '【悠学】《${homeworkName}》你申诉的${studentName}的题目已被打回，已验证，首次批改结果无误。#alidayu#SMS_127870031#alidayu#', b'0');
INSERT INTO `uxb`.`sms_template` (`code`, `content`, `note`, `body`, `mock`) VALUES ('10000032', NULL, NULL, '【悠学】《${homeworkName}》中申诉的题目已修正批改结果，赶紧打开查看吧。#alidayu#SMS_127685024#alidayu#', b'0');

-- 添加一个金币规则
INSERT INTO `uxb`.`coins_rule` (`code`, `action`, `coins_value`, `description`, `rule`) VALUES ('128', '28', '20', '错题申述', NULL);

-- 把未确认的题目全部修改成已确认
update student_homework_question set confirm_status = 2 where confirm_status = 1;
-- 作业表添加是否自动批改解答题属性
ALTER TABLE homework ADD COLUMN aq_auto_correct BIT(1) DEFAULT 0 COMMENT '是否自动批改解答题';
-- 作业表添加待批改标记
ALTER TABLE homework ADD COLUMN tobe_corrected BIT(1) DEFAULT 0 COMMENT '待批改标记';
-- 作业表添加全部批改完成标记
ALTER TABLE homework ADD COLUMN all_correct_complete BIT(1) DEFAULT 0 COMMENT '全部批改完成标记';
update homework set all_correct_complete=1 where right_rate is not null
-- 作业表添加正确率计算标记
ALTER TABLE homework ADD COLUMN right_rate_stat_flag BIT(1) DEFAULT 0 COMMENT '整份作业正确率统计标记';
update homework set right_rate_stat_flag=1 where right_rate is not null
-- 学生作业表添加批改状态（教师视角）
ALTER TABLE student_homework ADD COLUMN correct_status TINYINT(4) COMMENT '批改状态（教师视角）';
-- 学生作业表添加订正后的正确率
ALTER TABLE student_homework ADD COLUMN right_rate_correct decimal(10,0) COMMENT '订正后的正确率';
-- 学生作业表默认正确率为空值
ALTER TABLE student_homework ALTER COLUMN right_rate set default null;
-- 学生作业习题表添加预期批改方式
ALTER TABLE student_homework_question ADD COLUMN correct_type TINYINT(4) COMMENT '预期批改方式';
-- 学生作业习题表添加预期批改方式
ALTER TABLE student_homework_question ADD COLUMN correct_final_type TINYINT(4) COMMENT '最终批改方式';
-- 学生作业习题表添加订正完成标记
ALTER TABLE student_homework_question ADD COLUMN is_revised BIT(1) DEFAULT 0 COMMENT '订正完成标记';
-- 学生作业习题表添加新订正题标记
alter table student_homework_question add column `new_correct` bit(1) DEFAULT b'0' COMMENT '新订正标记';

-- 新建留言表
CREATE TABLE `homework_message` (
  `id` bigint(20) NOT NULL,
  `scene` tinyint(4) DEFAULT NULL,
  `message_type` tinyint(4) DEFAULT NULL,
  `biz_id` bigint(20) DEFAULT NULL,
  `icon_key` varchar(20) DEFAULT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `voice_time` smallint(5) DEFAULT NULL,
  `voice_file_key` varchar(50) DEFAULT NULL,
  `create_at` datetime(3) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `user_type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_creator` (`creator`) USING BTREE,
  KEY `idx_biz_id` (`biz_id`) USING BTREE
);

-- 新建申诉表
CREATE TABLE `question_appeal` (
  `id` bigint(20) NOT NULL,
  `appeal_type` tinyint(4) DEFAULT NULL,
  `question_source` tinyint(4) DEFAULT NULL,
  `biz_id` bigint(20) DEFAULT NULL,
  `appeal_right_rate` smallint(5) DEFAULT NULL,
  `appeal_correct_user_id` bigint(20) DEFAULT NULL,
  `item_results` varchar(128) DEFAULT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `create_at` datetime(3) DEFAULT NULL,
  `creator` bigint(20) DEFAULT NULL,
  `user_type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `coins` smallint(5) DEFAULT NULL,
  `correct_type` tinyint(4) DEFAULT NULL,
  `correct_item_results` varchar(128) DEFAULT NULL,
  `result` tinyint(4) DEFAULT NULL,
  `correct_result` tinyint(4) DEFAULT NULL,
  `correct_appeal_right_rate` smallint(5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_creator` (`creator`) USING BTREE,
  KEY `idx_biz_id` (`biz_id`) USING BTREE
);

-- 新建习题批改日志表
CREATE TABLE `question_correct_log` (
  `id` bigint(20) NOT NULL,
  `student_homework_question_id` bigint(20) DEFAULT NULL,
  `create_at` datetime(3) DEFAULT NULL,
  `result` tinyint(4) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `correct_type` tinyint(4) DEFAULT NULL,
  `auto_methods` varchar(500) DEFAULT NULL,
  `question_source` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_student_homework_question_id` (`student_homework_question_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
);

-- 新建批改习题传输失败记录表
CREATE TABLE `correct_question_fail_record` (
  `id` bigint(20) NOT NULL,
  `question_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  `question_type` tinyint(4) NOT NULL,
  `question_source` tinyint(4) NOT NULL,
  `question_category` tinyint(4) NOT NULL,
  `biz_id` bigint(20) NOT NULL,
  `create_at` datetime(3) DEFAULT NULL,
  `fail_count` int(6) DEFAULT '1',
  `success_at` datetime(3) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_question_id` (`question_id`) USING BTREE,
  KEY `idx_biz_id` (`biz_id`) USING BTREE
);


-- 新建批改员反馈记录表
CREATE TABLE correct_question_error (
 id BIGINT(20) NOT NULL,
 student_question_id BIGINT(20) NOT NULL,
 question_id BIGINT(20) NOT NULL,
 question_error_id BIGINT(20) NOT NULL,
 user_id BIGINT(20) NOT NULL,
 create_at DATETIME(3) DEFAULT NULL,
 PRIMARY KEY (id),
  KEY `idx_student_question_id` (`student_question_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
);