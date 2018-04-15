#macro($taskListStudent(ids))
SELECT * FROM student WHERE id IN (:ids)
#end