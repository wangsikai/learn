## 查找班级对应知识点前10名的正确率
#macro($queryByClass(classId,codes))
SELECT t.* FROM diagno_class_topn_kp t WHERE t.class_id = :classId AND t.knowpoint_code IN :codes
#end