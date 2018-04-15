## 通过教材代码获取预置练习
#macro($zycFindByTextbook(textbookCode))
SELECT * FROM textbook_exercise WHERE textbook_code = :textbookCode  and status in (0,1) ORDER BY section_code ASC,id ASC
#end

##获取当前章节此名称是否已经存在
#macro($getExerciseNameCount(sectionCode,name))
select count(id) from textbook_exercise where section_code=:sectionCode and name = :name
#end