##清表
#macro($taskDeleteKpStat())
DELETE FROM do_question_stu_kp_stat
#end

##查询表对象
#macro($getDoQuestionStuKnowpointStat(knowpointCode,studentId))
select * from do_question_stu_kp_stat where knowpoint_code = :knowpointCode and student_id =:studentId
#end
