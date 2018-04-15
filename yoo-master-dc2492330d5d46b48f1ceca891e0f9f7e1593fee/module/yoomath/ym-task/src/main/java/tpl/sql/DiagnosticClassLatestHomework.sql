## 删除最近30次作业记录
#macro($ymDeleteByClassId(classId))
DELETE FROM diagno_class_latest_hk WHERE class_id = :classId
#end

## 更新班级排名
#macro($ymUpdateByHomeworkId(rank,homeworkId))
UPDATE diagno_class_latest_hk SET class_rank = :rank WHERE homework_id = :homeworkId
#end
