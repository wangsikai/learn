## 根据班级id及知识点查找数据
#macro($ymFindByClassAndKnowpoint(classId,knowpoint,times))
SELECT * FROM diagno_class_latest_hk_kp t 
WHERE t.class_id = :classId AND t.knowpoint_code = :knowpoint AND times =:times
#end

#macro($ymDeleteByClassId(classId))
DELETE FROM diagno_class_latest_hk_kp WHERE class_id = :classId
#end