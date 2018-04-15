## 根据班级查询普通作业列表
#macro($csList(classId))
SELECT * FROM homework where homework_class_id = :classId and del_status = 0 and status != 0
#end