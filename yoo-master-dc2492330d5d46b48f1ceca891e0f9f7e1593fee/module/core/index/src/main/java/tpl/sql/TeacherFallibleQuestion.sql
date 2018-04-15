#macro($indexQueryByPage())
SELECT tfq.* FROM teacher_fallible_question tfq
#end

#macro($indexmget(ids))
SELECT tfq.* FROM teacher_fallible_question tfq WHERE id IN :ids
#end

##查询当前实际个数
#macro($indexDataCount())
SELECT count(id) FROM teacher_fallible_question
#end
