#macro($findQuestionByCode(codes,status))
select * from question where code in(:codes) 
#if(status)
and status = 2
#end
#end

#macro($findQuestionIdsByCode(codes,isOrderDif))
select id from question where code in(:codes) and status = 2
#if(isOrderDif)
order by type asc,difficulty desc
#end
#end

##查找作业已提交学生的题目
#macro($zycFindStuHKQuestions(size,notInIds,lastDate,nowDate))
SELECT q.*, sh.id as studentHomeworkId, h.id as homeworkId, sq.id studentQuestionId FROM student_homework sh, student_homework_question sq, homework h, question q
WHERE sh.id = sq.student_homework_id AND sh.status = 1 AND sh.del_status = 0
AND sh.homework_id = h.id AND sq.result in (2, 3) AND sq.sub_flag = 0 and sq.question_id = q.id
AND sq.auto_correct = 1 AND sq.manual_correct = 0 AND q.type IN (3, 5, 6)
AND (h.last_commit_at is null or h.last_commit_at >= :lastDate)
#if(notInIds)
AND sq.id NOT IN :notInIds
#end
LIMIT :size
#end

##查找作业下的所有题目
#macro($zycFindHKQuestions(hkId))
SELECT q.* FROM question q, homework_question hq
WHERE q.id = hq.question_id AND hq.homework_id = :hkId
ORDER BY hq.sequence ASC
#end

#macro($zycCountNotCorrectedQuestions(lastDate,nowDate))
SELECT count(*) FROM student_homework sh, student_homework_question sq, homework h, question q
WHERE sh.id = sq.student_homework_id AND sh.status = 1 AND sh.del_status = 0
AND sh.homework_id = h.id AND sq.result in (2, 3) AND sq.sub_flag = 0 and sq.question_id = q.id
AND sq.auto_correct = 1 AND sq.manual_correct = 0 AND q.type IN (3, 5, 6)
AND (h.last_commit_at is null or h.last_commit_at >= :lastDate)
#end

#macro($zycGetByCodes(codes))
SELECT * FROM question q WHERE q.code IN :codes
#end

##更新题目的学校ID
#macro($zycUpdateSchool(id,ids,schoolId))
UPDATE question SET school_id = :schoolId 
WHERE
#if(id)
id = :id
#end
#if(ids)
id IN (:ids)
#end
#end

## 获得题目的编号
#macro($zycGetQuestionCode(id))
SELECT t.code FROM question t WHERE t.id = :id
#end

## 根据学生作业查找其题目Question对象
#macro($zycFindByStuHk(stuHkId))
SELECT q.* FROM question q INNER JOIN student_homework_question shq ON shq.question_id = q.id
WHERE shq.student_homework_id = :stuHkId
#end

## 普通作业查看待确认题目
#macro($zycFindCorrected())
SELECT q.*, shq.id studentQuestionId, sh.id studentHomeworkId, sh.homework_id homeworkId, shq.new_correct newCorrect FROM question q 
INNER JOIN student_homework_question shq ON shq.question_id = q.id
INNER JOIN student_homework sh ON shq.student_homework_id = sh.id
WHERE shq.confirm_status = 1
#end

## 普通作业查看待确认题目
#macro($zycFindCorrectedById(questionCode))
SELECT q.*, shq.id studentQuestionId, sh.id studentHomeworkId, sh.homework_id homeworkId, shq.new_correct newCorrect FROM question q 
INNER JOIN student_homework_question shq ON shq.question_id = q.id
INNER JOIN student_homework sh ON shq.student_homework_id = sh.id
WHERE shq.confirm_status = 1
and q.code = :questionCode
#end