## 根据学生作业题目id查找最新的一条批改记录
#macro($findNewestLog(stuHkQId))
SELECT * FROM question_correct_log WHERE student_homework_question_id = :stuHkQId 
ORDER BY create_at DESC limit 1
#end


## 根据学生作业题目id查找最新的一条批改记录
#macro($getCorrectLogCount(stuHkQId,type,time))
SELECT count(*) FROM question_correct_log WHERE student_homework_question_id = :stuHkQId and create_at > :time and correct_type = :type
#end

