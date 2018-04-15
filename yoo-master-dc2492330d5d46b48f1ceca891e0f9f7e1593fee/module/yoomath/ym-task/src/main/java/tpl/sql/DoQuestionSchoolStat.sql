#macro($taskDeleteOldDoQuestionSchoolStat())
DELETE FROM do_question_school_stat WHERE status = 0;
#end

#macro($taskEnableNewDoQuestionSchoolStat())
UPDATE do_question_school_stat SET status = 0 WHERE status = 1;
#end

#macro($taskFindOne(day0,schoolId,phaseCode,classId))
SELECT * FROM do_question_school_stat 
WHERE status = 1 AND day0 = :day0 AND school_id = :schoolId AND phase_code = :phaseCode AND class_id = :classId
#end