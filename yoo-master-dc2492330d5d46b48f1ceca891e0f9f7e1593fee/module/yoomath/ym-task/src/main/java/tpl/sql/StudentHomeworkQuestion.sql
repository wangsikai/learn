##查询学生作业对应的题目
#macro($findQuestions(stuHkIds))
SELECT * FROM student_homework_question WHERE student_homework_id in :stuHkIds
#end

##查询学生普通作业题目列表
#macro($stuCommonHkList(nowTime))
	SELECT b.question_id,a.student_id,b.id FROM student_homework a
	INNER JOIN student_homework_question b ON a.id = b.student_homework_id
	WHERE a.submit_at IS NOT NULL AND a.stu_submit_at IS NOT NULL AND a.status != 0
	AND a.submit_at <= :nowTime
	AND b.id < :next ORDER BY b.id DESC
#end

##查询学生假期作业题目列表
#macro($stuHolidayHkList(nowTime))
	SELECT b.question_id,a.student_id,b.id FROM holiday_stu_homework_item a
	INNER JOIN holiday_stu_homework_item_question b ON a.id = b.holiday_stu_homework_item_id
	WHERE a.submit_at IS NOT NULL AND a.status != 0
	AND a.submit_at <= :nowTime
	AND b.id < :next ORDER BY b.id DESC
#end

##查询学生除学生和假期之外的题目列表
#macro($stuOtherExerciseList(nowTime))
	SELECT question_id,student_id,id FROM student_question_answer 
	WHERE source NOT IN(0,6,7,8)
	AND create_at <:nowTime
	AND id < :next ORDER BY id DESC
#end


##查询老师普通作业列表
#macro($teaCommonHkList(nowTime))
	SELECT b.create_id,a.question_id,a.id FROM homework_question a 
	INNER JOIN homework b ON a.homework_id = b.id
	AND b.start_time <:nowTime
	AND a.id < :next ORDER BY a.id DESC
#end

##查询老师假期作业列表
#macro($teaHolidayHkList(nowTime))
	SELECT c.create_id,a.question_id,a.id FROM holiday_homework_item_question a 
	INNER JOIN holiday_homework_item b ON a.holiday_homework_item_id = b.id
	INNER JOIN holiday_homework c ON b.holiday_homework_id = c.id
	AND c.start_time <:nowTime
	AND a.id < :next ORDER BY a.id DESC
#end

##查询学生除学生和假期之外的题目列表，小于指定studentQuesitonAnswerId
#macro($stuExerciseListByStuQuestionAnswerId())
	SELECT question_id,student_id,id FROM student_question_answer 
	WHERE source NOT IN(0,6,7,8)
	AND id < :next ORDER BY id DESC
#end

