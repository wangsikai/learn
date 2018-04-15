## 查询班级教材码(加入时间顺序)
#macro($getClassTextbooks(classId,category))
SELECT t.textbook_code FROM diagno_class_textbook t WHERE t.class_id = :classId AND t.textbook_code LIKE :category
ORDER BY t.update_at DESC
#end

## 查询班级教材码(按教材自身顺序)
#macro($getClassSortTextbooks(classId,category))
SELECT t.textbook_code FROM diagno_class_textbook t WHERE t.class_id = :classId AND t.textbook_code LIKE :category
ORDER BY t.textbook_code
#end
