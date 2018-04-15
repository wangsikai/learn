#macro($taskDeleteOldDoQuestionClassStat())
DELETE FROM do_question_class_stat WHERE status = 0;
#end

#macro($taskEnableNewDoQuestionClassStat())
UPDATE do_question_class_stat SET status = 0 WHERE status = 1;
#end

#macro($taskQueryDoQuestionClassStat())
SELECT * FROM do_question_class_stat WHERE status = 0 AND school_id > 0 AND phase_code > 0 AND id < :next ORDER BY id DESC
#end

#macro($taskListOneClass(classId))
SELECT * FROM do_question_class_stat WHERE status = 0 AND class_id = :classId
#end