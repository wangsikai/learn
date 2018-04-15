#macro($indexmget(ids))
SELECT * FROM question_collection WHERE id IN :ids
#end

#macro($indexQueryByPage())
SELECT * FROM question_collection
#end

## 查询当前实际个数
#macro($indexDataCount())
SELECT count(id) FROM question_collection
#end
