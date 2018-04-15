## 更新签名介绍
#macro($updateIntroduce(userId,introduce))
UPDATE student SET introduce = :introduce WHERE id = :userId
#end

## 查找设置过教材的学生
#macro(findByTextbookNotNull())
SELECT s.* FROM student s WHERE s.textbook_code > 0
#end
