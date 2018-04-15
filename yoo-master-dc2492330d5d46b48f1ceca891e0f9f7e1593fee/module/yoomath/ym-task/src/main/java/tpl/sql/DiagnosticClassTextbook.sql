## 根据班级id及教材编码查找相关数据
#macro($ymFindByClassAndTextbook(textbookCode,classId))
SELECT * FROM diagno_class_textbook t WHERE t.class_id = :classId AND t.textbook_code = :textbookCode
#end

## 根据班级id及教材编码查找相关数据
#macro($ymFindByClassAndTextbooks(textbookCodes,classId))
SELECT * FROM diagno_class_textbook t WHERE t.class_id = :classId AND t.textbook_code in (:textbookCodes)
#end

## 根据班级删除数据
#macro($taskDelByClass(classId))
delete from diagno_class_textbook where class_id=:classId
#end

## 根据diagno_class_kp表获取教材列表
#macro($findTextbookByDiaClassKp(classId))
SELECT tb.* FROM textbook tb
WHERE
EXISTS (
 SELECT 1 FROM section s
 INNER JOIN knowledge_section ks ON ks.section_code=s.code
 INNER JOIN diagno_class_kp dck ON dck.knowpoint_code=ks.knowledge_code AND dck.do_count>0 AND dck.class_id=:classId
 WHERE s.textbook_code = tb.code AND s.status = 0
)
#end