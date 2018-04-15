## 根据班级id及知识点进行查询
#macro($ymFindByClassAndKp(classId,code))
SELECT t.* FROM diagno_class_topn_kp t WHERE t.class_id = :classId AND t.knowpoint_code = :code
#end

## 删除班级的topn数据
#macro($deleteTopn(classId))
DELETE FROM diagno_class_topn_kp WHERE class_id = :classId
#end