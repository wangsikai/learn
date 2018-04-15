##根据班级和知识点查询班级知识点统计
#macro($getHkClazzNewKp(classId))
SELECT * FROM homework_class_knowpoint_stat WHERE class_id = :classId
#end

#macro($deleteNewAll())
DELETE FROM homework_class_knowpoint_stat 
#end