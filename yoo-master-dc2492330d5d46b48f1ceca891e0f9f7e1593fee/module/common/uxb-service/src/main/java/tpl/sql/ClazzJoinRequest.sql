##  查找学生待加入班级数据
#macro($zyFindClazzRequest(classId,studentId))
SELECT t.* FROM clazz_join_request t WHERE t.student_id = :studentId AND t.homework_class_id = :classId AND t.request_status = 0 AND t.delete_status = 0
#end

##  申请的数量
#macro($zyRequestCount(teacherId,startTime))
select count(id) from clazz_join_request where teacher_id = :teacherId and request_status = 0 AND delete_status = 0
#if(startTime)
	and update_at > :startTime
#end
#end

##  申请列表
#macro($zyRequestList(teacherId))
select * from clazz_join_request where teacher_id = :teacherId and delete_status = 0 order by update_at desc
#end

## 查询教师待处理的列表数量
#macro($zyCountConfirmStuNum(teacherId))
SELECT count(id) FROM clazz_join_request WHERE teacher_id = :teacherId AND request_status = 0 AND delete_status = 0
#end

## 游标查询教师待处理的申请列表
#macro($zyCursorRequestList(teacherId))
select * from clazz_join_request where teacher_id = :teacherId AND id < :next AND delete_status = 0 order by update_at desc
#end

## 删除请求数据
#macro($delete(id,updateAt))
UPDATE clazz_join_request SET delete_status = 1, update_at = :updateAt WHERE id = :id
#end
