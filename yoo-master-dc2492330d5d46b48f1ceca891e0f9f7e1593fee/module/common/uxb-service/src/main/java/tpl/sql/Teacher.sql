## 更新签名介绍
#macro($updateIntroduce(userId,introduce))
UPDATE teacher SET introduce = :introduce WHERE id = :userId
#end

##由用户名获取teacher
#macro($getTeacherByName(name))
SELECT * FROM teacher WHERE name=:name
#end

##分页获取teacher
#macro($getAllByPage())
SELECT * FROM teacher WHERE id < :next ORDER BY id DESC
#end

##更新教师教材版本
#macro($uptTeacherCategory(textbookCategoryCode,textBookCode,userId))
UPDATE teacher SET textbook_category_code=:textbookCategoryCode ,textbook_code=:textBookCode WHERE id=:userId
#end