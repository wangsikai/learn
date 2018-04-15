## 根据作业id获取作业题目列表
#macro($zyFindByHomework(homeworkIds))
SELECT * FROM homework_question WHERE homework_id IN (:homeworkIds) ORDER BY sequence ASC
#end

## 根据单个作业id获取作业题目列表
#macro($getClazzQuestionStat(homeworkId))
SELECT right_rate FROM homework_question WHERE homework_id= :homeworkId ORDER BY sequence ASC
#end

##题目做错学生
#macro($listWrongStu(homeworkId,questionId))
SELECT sh.student_id,stha.* FROM student_homework sh INNER JOIN student_homework_question shq ON shq.student_homework_id =sh.id  
INNER JOIN student_homework_answer stha ON stha.student_homework_question_id =shq.id WHERE sh.homework_id=:homeworkId
AND shq.question_id =:questionId AND shq.result=2 AND sh.submit_at IS NOT NULL AND sh.stu_submit_at IS NOT NULL
#end

## 更新作业题目的相关统计
#macro($zyUpdateHomeworkQuestionStat(hkId,questionId,rightRate,rightCount,wrongCount,halfWrongCount,doTime))
UPDATE homework_question SET right_rate = :rightRate,right_count = :rightCount,wrong_count = :wrongCount,half_wrong_count = :halfWrongCount, do_time = :doTime
WHERE question_id = :questionId AND homework_id = :hkId
#end

## 得到需要批改的题目列表
#macro($queryNeedCorrectQuestions(homeworkId))
SELECT hq.* FROM homework t
INNER JOIN homework_question hq ON hq.homework_id = t.id
INNER JOIN student_homework sh ON sh.homework_id = t.id
WHERE
  sh.id IN
  (SELECT sh.id FROM student_homework sh
   INNER JOIN student_homework_question shq ON shq.student_homework_id = sh.id
   INNER JOIN question q ON shq.question_id = q.id
   WHERE shq.auto_correct = 1 AND q.type IN (3, 5) AND sh.submit_at IS NOT NULL
  ) AND t.id = :homeworkId
#end

## 根据单个作业id获取作业题目列表
#macro($findQuestionByHomework(homeworkId))
SELECT q.* FROM homework_question t
 INNER JOIN question q ON q.id=t.question_id
 WHERE t.homework_id=:homeworkId AND t.status = 0
#end