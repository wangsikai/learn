#macro($indexmget(ids))
SELECT * FROM student_fallible_question WHERE id IN :ids
#end

#macro($indexQueryByPage())
SELECT * FROM student_fallible_question
#end

## 查询当前实际个数
#macro($indexDataCount())
SELECT count(id) FROM student_fallible_question
#end