#macro($indexmget(ids))
SELECT * FROM school_question WHERE id IN :ids
#end

#macro($indexQueryByPage())
SELECT * FROM school_question
#end

## 查询当前实际个数
#macro($indexDataCount())
SELECT count(id) FROM school_question
#end