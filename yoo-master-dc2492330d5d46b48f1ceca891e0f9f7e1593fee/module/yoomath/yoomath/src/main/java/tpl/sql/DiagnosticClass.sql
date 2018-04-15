## 根据教材及班级查询数据
#macro($getByTextbook(code,classId))
SELECT t.* FROM diagno_class t WHERE t.class_id = :classId AND t.textbook_code = :code
#end