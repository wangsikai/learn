##查询学生错题下载记录
#macro($query(studentId))
SELECT t.* FROM student_fallible_export_record t 
 WHERE t.student_id = :studentId AND t.status = 0 order by t.create_at DESC
#end

##根据hash查询学生错题下载记录
#macro($findByHash(studentId,hash))
SELECT t.* FROM student_fallible_export_record t 
 WHERE t.student_id = :studentId AND t.hash=:hash AND t.status = 0 order by t.create_at DESC
#end