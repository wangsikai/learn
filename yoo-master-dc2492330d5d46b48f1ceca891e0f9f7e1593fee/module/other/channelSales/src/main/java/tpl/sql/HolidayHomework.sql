## 根据班级查询假期作业列表
#macro($csList(classId))
SELECT * FROM holiday_homework where homework_class_id = :classId and del_status = 0 and status != 0
#end