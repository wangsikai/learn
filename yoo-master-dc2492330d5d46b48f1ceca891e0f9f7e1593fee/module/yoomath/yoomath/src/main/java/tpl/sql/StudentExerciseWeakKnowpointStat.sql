##获取学生薄弱知识点
#macro($queryWeakKnowpoints(studentId,phaseCode,count1))
SELECT knowpoint_code FROM student_exercise_weak_knowpoint_stat WHERE student_id = :studentId AND phase_code = :phaseCode
ORDER BY RAND() LIMIT 0,:count1
#end

