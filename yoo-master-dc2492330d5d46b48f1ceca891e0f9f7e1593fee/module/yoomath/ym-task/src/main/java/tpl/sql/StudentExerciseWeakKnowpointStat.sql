##获取学生薄弱知识点统计对象
#macro($ymGetWeakStat(studentId,knowpointCode))
select * from student_exercise_weak_knowpoint_stat where student_id = :studentId and knowpoint_code=:knowpointCode
#end

##获取学生薄弱知识点统计对象
#macro($ymClearWeakStat())
delete from student_exercise_weak_knowpoint_stat
#end

