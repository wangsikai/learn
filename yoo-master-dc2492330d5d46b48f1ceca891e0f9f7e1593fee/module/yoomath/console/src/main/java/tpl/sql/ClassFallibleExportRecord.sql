##查询学生错题下载记录
#macro($query())
SELECT t.* FROM class_fallible_export_record t order by t.create_at DESC
#end

##根据classid和状态查询
#macro($queryByclassId(classId,status))
SELECT t.* FROM class_fallible_export_record t where t.class_id=:classId and t.status=:status order by t.create_at DESC
#end
