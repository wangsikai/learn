## 查询一次作业学生的错题
#macro($zyFindHomeworkWrongQuestion(homeworkId))
select t4.* from homework_question t3 INNER JOIN(
select 
t1.student_id,t2.question_id
from 
student_homework t1 INNER JOIN
student_homework_question t2 ON t1.id = t2.student_homework_id
where t1.homework_id = :homeworkId and t2.result = 2
) t4 ON t3.question_id = t4.question_id where t3.homework_id = :homeworkId
order by t4.student_id,t3.sequence
#end


## 获取学生一次作业的订正题
#macro($zyGetCorrectQuestions(stuHkId))
SELECT question_id 
FROM student_homework_question 
WHERE student_homework_id = :stuHkId AND correct = 1 AND sub_flag = 0 ORDER BY id ASC
#end
 
## 获取学生一次作业的订正题 小优快批
#macro($zyGetNewCorrectQuestions(stuHkId))
SELECT question_id 
FROM student_homework_question 
WHERE student_homework_id = :stuHkId AND new_correct = 1 AND sub_flag = 0 ORDER BY id ASC
#end

## 统计此次作业的总体正确率
## @since 小悠快批，2018-3-1，添加新订正题字段条件
#macro($zyStatisticHomework(hkId))
SELECT count(t1.id) cou,t1.result,t1.right_rate,t1.type
FROM student_homework_question t1 
INNER JOIN student_homework t2 ON t1.student_homework_id = t2.id 
INNER JOIN homework t3 ON t2.homework_id  = t3.id
WHERE t3.id = :hkId AND t1.correct = 0 AND t1.new_correct = 0 AND t1.sub_flag = 0 AND t2.submit_at IS NOT NULL AND t2.stu_submit_at IS NOT NULL
GROUP BY t1.result,t1.right_rate,t1.type
#end

## 统计每题的正确率情况
## @since 小悠快批，2018-3-1，添加新订正题字段条件
#macro($zyStatisticHomeworkQuestion(hkId))
SELECT count(t1.id) cou,t1.question_id,t1.result,t1.type,SUM(t1.right_rate) right_rate
FROM student_homework_question t1 
LEFT JOIN student_homework t2 ON t1.student_homework_id = t2.id 
LEFT JOIN homework t3 ON t2.homework_id  = t3.id
WHERE t3.id = :hkId AND t1.correct = 0 AND t1.new_correct = 0 AND t1.sub_flag = 0 AND t2.submit_at IS NOT NULL AND t2.stu_submit_at IS NOT NULL
GROUP BY t1.result,t1.question_id,t1.type ORDER BY t1.question_id;
#end

## 统计每一题平均答题时间
## @since 小悠快批，2018-3-1，添加新订正题字段条件
#macro($zyStatisticQuestionDoTime(hkId))
SELECT t1.question_id, avg(t1.do_time) do_time
FROM student_homework_question t1
LEFT JOIN student_homework t2 ON t1.student_homework_id = t2.id
LEFT JOIN homework t3 ON t2.homework_id  = t3.id
WHERE t3.id = :hkId AND t1.correct = 0 AND t1.new_correct = 0 AND t1.sub_flag = 0 AND t2.submit_at IS NOT NULL AND t2.stu_submit_at IS NOT NULL
GROUP BY t1.question_id ORDER BY t1.question_id;
#end

## 统计简答题半对的数量
## @since 小悠快批，2018-3-1，添加新订正题字段条件
#macro($zyStatisticHomeworkQuestionHalfWrongCount(hkId))
SELECT count(t1.id) cou,t1.question_id 
FROM student_homework_question t1 
LEFT JOIN student_homework t2 ON t1.student_homework_id = t2.id 
LEFT JOIN homework t3 ON t2.homework_id  = t3.id
WHERE t3.id = :hkId AND t1.correct = 0 AND t1.new_correct = 0 AND t1.sub_flag = 0 AND t2.submit_at IS NOT NULL AND t2.stu_submit_at IS NOT NULL
AND (t1.type = 3 OR t1.type = 5) AND t1.right_rate IS NOT null AND t1.right_rate < 100 AND t1.right_rate > 0
GROUP BY t1.question_id
#end

## 统计每个学生的正确率情况
## @since 小悠快批，2018-3-1，添加新订正题字段条件
#macro($zyStatisticHomeworkStudent(hkId))
SELECT count(t1.id) cou,t1.result,t2.student_id 
FROM student_homework_question t1 
LEFT JOIN student_homework t2 ON t1.student_homework_id = t2.id 
LEFT JOIN homework t3 ON t2.homework_id  = t3.id
WHERE t3.id = :hkId AND t1.correct = 0 AND t1.new_correct = 0 AND t1.sub_flag = 0 AND t2.submit_at IS NOT NULL AND t2.stu_submit_at IS NOT NULL
GROUP BY t1.result,t2.student_id ORDER BY t2.student_id;
#end

## 得到待批改的学生题目列表
## @since 小悠快批，2018-3-1，添加新订正题字段取值
#macro($zyListUncorrectStu(homeworkId,questionId,questionType))
SELECT shq.id, shq.student_homework_id, shq.question_id, shq.result, shq.comment, shq.correct_at, shq.do_time,shq.voice_time,
shq.sub_flag, shq.correct,shq.new_correct, shq.auto_correct, shq.manual_correct, shq.answer_img, shq.notation_answer_img, shq.right_rate, shq.type, "" AS answer_notation,
shq.confirm_status, "" AS answer_notation_points, shq.notation_mobile_img, shq.notation_web_img, shq.voice_file_key,shq.correct_type,shq.is_revised,shq.new_correct
FROM student_homework sh INNER JOIN student_homework_question shq ON shq.student_homework_id =sh.id
WHERE sh.homework_id=:homeworkId
AND shq.auto_correct = 1
AND shq.question_id =:questionId
#if(questionType==3)
AND shq.result = 3
#end
AND shq.correct_type=3
AND sh.submit_at IS NOT NULL AND sh.stu_submit_at IS NOT NULL
#end

## 根据学生作业ID和习题ID获取作业习题(不对轨迹字段进行查询操作.)
## @since 小悠快批，2018-3-1，添加新订正题字段取值
#macro($zyFindByStudentHomeworkIdAndQuestionId(studentHomeworkId,questionId,questionIds))
SELECT shq.id, shq.student_homework_id, shq.question_id, shq.result, shq.comment, shq.correct_at, shq.do_time,
shq.sub_flag, shq.correct,shq.new_correct, shq.auto_correct, shq.manual_correct, shq.answer_img, shq.notation_answer_img, shq.right_rate, shq.type, "" AS answer_notation,
shq.confirm_status, "" AS answer_notation_points, shq.notation_mobile_img, shq.notation_web_img, shq.voice_time, shq.voice_file_key
FROM student_homework_question shq
WHERE student_homework_id = :studentHomeworkId
#if(questionId)
AND question_id = :questionId
#end
#if(questionIds)
AND question_id IN :questionIds
#end
#end

## 更新旋转后的图片
#macro($zyUpdateAnswerImage(id,image))
UPDATE student_homework_question SET answer_img = :image WHERE id = :id
#end


##查询一次作业中的错题列表
#macro($listCorrectQuestions(hkId))
SELECT distinct(a.question_id) FROM 
student_homework_question a 
INNER JOIN student_homework b ON a.student_homework_id = b.id 
INNER JOIN homework c ON b.homework_id = c.id
WHERE c.id = :hkId AND a.correct = 1
#end


## 查找某份作业下面的某道题的所有学生作业题目列表
#macro($zyFindByQuestionId(homeworkId,questionId))
SELECT t1.* 
FROM student_homework_question t1 
INNER JOIN student_homework t2 ON t1.student_homework_id = t2.id
WHERE t2.homework_id = :homeworkId AND t1.question_id = :questionId
#end

## 查询这份作业知识点的对错情况
#macro($findStuQuestionMapByOldCodes(studentHomeworkQuestionId,codes))
	SELECT b.meta_code code,a.result,COUNT(1) count FROM student_homework_question a
	INNER JOIN question_metaknow b ON a.question_id = b.question_id
	WHERE student_homework_id = :studentHomeworkQuestionId
	#if(codes)
		and b.meta_code in :codes
	#end
	GROUP BY b.meta_code,a.result
#end


## 查询这份作业知识点的对错情况
#macro($findStuQuestionMapByNewCodes(studentHomeworkQuestionId,codes))
	SELECT b.knowledge_code code,a.result,COUNT(1) count FROM student_homework_question a
	INNER JOIN question_knowledge b ON a.question_id = b.question_id
	WHERE student_homework_id = :studentHomeworkQuestionId
	#if(codes)
		and b.knowledge_code in :codes
	#end
	GROUP BY b.knowledge_code,a.result
#end


## 统计某份作业下所有习题的答题对错情况（教学诊断统计-下发作业后使用 2017-7-5） 
## @since 小悠快批，2018-3-1，添加新订正题字段条件
#macro($ymFindAllQuestionResultFromHomework(homeworkIds,studentIds))
SELECT tb.question_id,SUM(CASE tb.result WHEN 1 THEN tb.num END) AS right_count,
 SUM(CASE tb.result WHEN 0 THEN tb.num WHEN 2 THEN tb.num WHEN 3 THEN tb.num END) AS wrong_count,tb.difficulty,tb.homework_id
 FROM (
 SELECT COUNT(t.id) AS num,t.result,t.question_id,q.difficulty,sh.homework_id FROM student_homework_question t
 INNER JOIN question q ON q.id=t.question_id
 INNER JOIN student_homework sh ON sh.id=t.student_homework_id AND sh.submit_at IS NOT NULL AND sh.stu_submit_at IS NOT NULL
 #if(studentIds)
  AND sh.student_id in (:studentIds)
 #end
 INNER JOIN homework h ON h.id=sh.homework_id AND h.id in (:homeworkIds)
 WHERE t.sub_flag = 0 AND t.correct=0 AND t.new_correct=0 AND EXISTS(
  SELECT 1 FROM homework_student_class hsc WHERE hsc.class_id=h.homework_class_id AND hsc.student_id=sh.student_id AND hsc.status=0
 )
 GROUP BY t.question_id,t.result
) tb GROUP BY tb.question_id
#end

## 统计某个班级、某个学生下所有习题的答题对错情况（教学诊断统计-下发作业后使用 2017-7-5） 
## @since 小悠快批，2018-3-1，添加新订正题字段条件
#macro($ymFindAllQuestionResultFromStudent(classId,studentId))
SELECT tb.question_id,SUM(CASE tb.result WHEN 1 THEN tb.num END) AS right_count,
 SUM(CASE tb.result WHEN 0 THEN tb.num WHEN 2 THEN tb.num WHEN 3 THEN tb.num END) AS wrong_count,tb.difficulty
  FROM (
 SELECT COUNT(t.id) AS num,t.result,t.question_id,q.difficulty FROM student_homework_question t
 INNER JOIN question q ON q.id=t.question_id
 INNER JOIN student_homework sh ON sh.student_id=:studentId and sh.id=t.student_homework_id AND sh.submit_at IS NOT NULL AND sh.stu_submit_at IS NOT NULL
 INNER JOIN homework h ON h.status=3 AND h.id=sh.homework_id 
 #if(classId)
  AND h.homework_class_id=:classId
 #end
 WHERE t.correct=0 AND t.new_correct=0
 GROUP BY t.question_id,t.result
) tb GROUP BY tb.question_id
#end

## 统计学生作业中的错题数和已订正题数目
#macro($findWrongAndcorrectionQuestion(studentHomeworkIds))
SELECT tt.id id,SUM(tt.wrong_count) wrong_count,SUM(tt.correct_count) correct_count FROM
(SELECT 
  shq.`student_homework_id` id ,COUNT(1) wrong_count,0 correct_count
FROM
  `student_homework_question` shq 
WHERE 1 = 1 
  AND shq.`new_correct` =1
  AND shq.`type` IN(1,2,3,4,5)
  AND shq.`student_homework_id` in (:studentHomeworkIds)
GROUP BY shq.`student_homework_id` 
UNION
SELECT 
  shq.`student_homework_id` id,0 wrong_count,COUNT(1) correct_count
FROM
  `student_homework_question` shq 
WHERE 1 = 1 
  AND shq.`new_correct` =1
  AND shq.`is_revised` = 1
  AND shq.`type` IN(1,2,3,4,5)
  AND shq.`student_homework_id` in (:studentHomeworkIds)
GROUP BY shq.`student_homework_id` 
)tt GROUP BY tt.id
#end

##统计正在人工批改的题和已经批改过的题
#macro($findCorrectingAndCorrectedQuestion(studentHomeworkIds))
SELECT 
  SUM(CASE shq.result WHEN 1 THEN 1 WHEN 2 THEN 1 ELSE 0 END) corrected_count,
  SUM(CASE shq.result WHEN 3 THEN 1 ELSE 0 END) correcting_count,
  shq.`student_homework_id` AS stu_homework_id 
FROM
  `student_homework_question` shq 
WHERE 1 = 1 
  AND shq.`student_homework_id` in (:studentHomeworkIds)
  AND shq.new_correct = 0
GROUP BY shq.`student_homework_id` 
#end

## 根据stuHomeworkId和题目id查询学生作业的题目信息
#macro($listByStuHomeworkIdAndQuestionId(stuHomeworkId,questionId))
SELECT t1.* FROM student_homework_question t1 
WHERE t1.student_homework_id = :stuHomeworkId
AND t1.question_id = :questionId
#end

## 统计学生作业待批改题数目(只有解答题才会有待批改标签)
#macro($staticToBeCorrectedQuestionCount(stuHomeworkIds))
SELECT shq.student_homework_id AS stuHomeworkId,count(1) AS toBeCorrectedCount FROM student_homework_question shq 
WHERE shq.correct_type IN (1,2,3,4) 
AND (shq.result = 0 or shq.result = 3)
AND shq.student_homework_id in (:stuHomeworkIds)
AND shq.new_correct = 0
GROUP BY shq.student_homework_id
#end