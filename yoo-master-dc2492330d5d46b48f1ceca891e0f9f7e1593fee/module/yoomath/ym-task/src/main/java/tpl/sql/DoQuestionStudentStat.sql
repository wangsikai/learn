#macro($taskDeleteOldDoQuestionStudentStat())
DELETE FROM do_question_student_stat WHERE status = 0;
#end

#macro($taskEnableNewDoQuestionStudentStat())
UPDATE do_question_student_stat SET status = 0 WHERE status = 1;
#end

#macro($taskQueryStudentInOneClass(day0,userIds))
SELECT * FROM do_question_student_stat WHERE status = 0 AND day0 = :day0 AND user_id IN (:userIds) ORDER BY do_count DESC,right_rate DESC
#end