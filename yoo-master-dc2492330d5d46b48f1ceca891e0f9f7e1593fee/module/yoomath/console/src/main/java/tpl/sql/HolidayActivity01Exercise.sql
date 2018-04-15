##获取最后一个生成的练习（练习初始化使用）
#macro($getLastTeacherExercises(activityCode, teacherId, type))
SELECT t.* FROM holiday_activity_01_exercise t
 WHERE t.user_id=:teacherId and t.type=:type and t.activity_code=:activityCode
 order by t.sequence DESC limit 1
#end

##删除所有练习（练习初始化使用）
#macro($deleteAll(activityCode))
DELETE FROM holiday_activity_01_exercise where activity_code=:activityCode AND type IN (1,2) and id>99999
#end

##获取当前所有关联题目的知识点（练习初始化使用）
#macro($listAllKnowpoint())
SELECT DISTINCT(t.knowledge_code) FROM question_knowledge t
#end

##根据知识点获取题目（练习初始化使用）
#macro($listQuestionByKnowpoint(knowledgeCode, num))
SELECT t.question_id from question_knowledge t where t.knowledge_code=:knowledgeCode ORDER BY RAND() limit :num
#end

##获取教师弱项知识点个数（练习初始化使用）
#macro($listTeacherWeakpointCount)
SELECT COUNT(1) as num,tb.teacher_id FROM (
 SELECT COUNT(1),c.`teacher_id`,t.`knowpoint_code` FROM `diagno_class_kp` t
 INNER JOIN `homework_class` c ON c.`id`=t.`class_id`
 WHERE LENGTH(t.`knowpoint_code`)=10 AND t.`create_at`>'2017-7-1' AND t.`create_at`<'2018-1-10'
 GROUP BY c.`teacher_id`,t.`knowpoint_code`
 ORDER BY c.`teacher_id` ASC,t.`knowpoint_code` ASC
) tb GROUP BY tb.teacher_id
#end

##获取教师弱项知识点（练习初始化使用）
#macro($listTeacherWeakpoint(teacherIds))
SELECT COUNT(1) as num,c.`teacher_id`,t.`knowpoint_code` FROM `diagno_class_kp` t
 INNER JOIN `homework_class` c ON c.`id`=t.`class_id` AND c.`teacher_id` in (:teacherIds)
 WHERE LENGTH(t.`knowpoint_code`)=10 AND t.`create_at`>'2017-7-1' AND t.`create_at`<'2018-1-10'
 GROUP BY c.`teacher_id`,t.`knowpoint_code`
 ORDER BY c.`teacher_id` ASC,t.`knowpoint_code` ASC
#end