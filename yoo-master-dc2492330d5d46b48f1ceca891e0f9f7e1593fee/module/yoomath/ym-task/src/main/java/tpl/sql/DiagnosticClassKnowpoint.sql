## 统计班级知识点做题情况
#macro($ymGetByClassAndKnowpoint(classId,code))
SELECT t.* FROM diagno_class_kp t WHERE t.class_id = :classId AND t.knowpoint_code = :code
#end

## 统计班级知识点做题情况
#macro($ymGetByClassAndKnowpoints(classId,codes))
SELECT t.* FROM diagno_class_kp t WHERE t.class_id = :classId AND t.knowpoint_code in (:codes)
#end

## 判断是否有数据
#macro($ymHasData())
SELECT count(*) from diagno_class_kp
#end