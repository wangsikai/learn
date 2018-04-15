##得到某个知识点下的掌握情况
#macro($ymGetByMetaAndStudentId(studentId,knowledgeCode))
SELECT t.* FROM student_exercise_knowpoint t WHERE t.knowpoint_code = :knowledgeCode AND t.student_id = :studentId
#end

##得到某个知识点下的掌握情况
#macro($ymGetByKpsAndStudentId(studentId,knowledgeCodes))
SELECT t.* FROM student_exercise_knowpoint t WHERE t.knowpoint_code IN :knowledgeCodes AND t.student_id = :studentId
#end

## 查找知识点数据
#macro($queryStuWeak(minRate))
SELECT (do_count-wrong_count)/do_count rightrate,student_id,knowpoint_code,id FROM student_exercise_knowpoint 
where id < :next and (do_count-wrong_count)/do_count <:minRate
ORDER BY id desc
#end
