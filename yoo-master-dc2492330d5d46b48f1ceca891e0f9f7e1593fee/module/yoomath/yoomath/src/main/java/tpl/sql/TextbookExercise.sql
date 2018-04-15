## 通过教材代码获取预置练习
#macro($zyFindByTextbook(textbookCode))
SELECT * FROM textbook_exercise WHERE textbook_code = :textbookCode ORDER BY section_code ASC,id ASC
#end

##通过章节获取习题册列表
#macro($getTbeListBySectioCode(sectionCode,type))
SELECT * FROM textbook_exercise WHERE section_code =:sectionCode and type =:type and status=0
#end
