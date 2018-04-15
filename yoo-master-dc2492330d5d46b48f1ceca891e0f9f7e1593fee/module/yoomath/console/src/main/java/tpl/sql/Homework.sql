##得到待处理的作业列表 finish是已截止，init是作业中，working是批改中
#macro($zycGetTodo(finish,working,init,flagDate,startTime,endTime,schoolName))
SELECT t.* FROM
(
  #if(finish==1)
  SELECT t.* FROM homework t WHERE t.status = 2 or t.status = 3
  #if(working==1||init==1)
  UNION
  #end
  #end
  #if(init==1)
  SELECT t.* FROM homework t WHERE t.status = 1
  #if(working==1)
  UNION
  #end
  #end
  #if(working==1)
  SELECT t.* FROM homework t WHERE (t.status = 1 or t.status = 2) and t.commit_count > 0 and t.all_correct_complete = 0
  #end
) t
INNER JOIN homework_class c ON c.id = t.homework_class_id
INNER JOIN teacher u ON t.create_id = u.id
LEFT JOIN school s ON u.school_id = s.id
WHERE t.correcting_type = 0
AND t.del_status = 0
#if(schoolName)
  AND s.name LIKE :schoolName
#end
#if(startTime)
  AND t.start_time >= :startTime
#end
#if(endTime)
  AND t.start_time <= :endTime
#end

ORDER BY t.start_time DESC
#end

##根据学生作业ID和题目序号获取标准答案
#macro($zycFindStandardAnswer(stuHkId,idx,qId))
select content from answer where question_id = :qId order by sequence asc;
#end

##根据学生作业ID和题目序号获取学生答案和订正答案
#macro($zycFindStudentAnswer(stuHkId,idx,qId,newCorrect,noCorrect))
select content from student_homework_answer where student_homework_question_id =(
SELECT
	t2.id
FROM
	student_homework t1
INNER JOIN student_homework_question t2 ON t1.id = t2.student_homework_id
WHERE
	t1.id = :stuHkId  and t2.question_id = :qId 
#if(newCorrect)
  AND t2.new_correct = 1
#end
#if(noCorrect)
  AND t2.new_correct = 0
#end
)
order by sequence asc;
#end

##根据学生作业ID和序号获取题目ID
#macro($zycQueryQuestionId(stuHkId,idx))
SELECT t2.question_id FROM student_homework t1 INNER JOIN homework_question t2
ON t1.homework_id = t2.homework_id WHERE t1.id = :stuHkId AND t2.sequence = :idx
#end

## 后台移除操作
#macro($zycRemoveHomework(homeworkId))
UPDATE homework SET man_status = 1 WHERE id = :homeworkId
#end

##获取时间范围内布置并且下发的作业
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($zycListByTime(beginTime,endTime,createId,clazzId))
SELECT * FROM homework WHERE create_at>:beginTime AND create_at<:endTime AND deadline>:beginTime AND deadline<:endTime
AND create_id=:createId AND homework_class_id=:clazzId AND (status = 3 or all_correct_complete = 1)
#end

##获取某个月内某个班级 第一份下发的作业
#macro($zycGetFirstIssuedHomeworkInMonth(beginTime,endTime,classId))
SELECT * FROM homework WHERE issue_at IS NOT NULL AND textbook_code IS NOT NULL AND issue_at>:beginTime AND issue_at<:endTime AND homework_class_id=:classId ORDER BY issue_at ASC limit 1
#end

##作业转让,没有下发的作业更新创建人
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($zycHomeworkTransfer(classId,newTeacherId))
	update homework set create_id = :newTeacherId where homework_class_id = :classId and status != 3 and all_correct_complete != 1
#end

