#macro($taskListclazz(studentIds))
SELECT * FROM homework_student_class WHERE status = 0 AND student_id IN (:studentIds)
#end