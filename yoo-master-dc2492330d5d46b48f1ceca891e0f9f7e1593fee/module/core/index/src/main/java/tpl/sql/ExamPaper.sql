#macro($indexQueryByPage())
SELECT * FROM exam_paper WHERE status != 5
#end

##删除状态不获取
#macro($indexmget(ids))
SELECT * FROM exam_paper WHERE status = !5 AND id IN :ids
#end

## 查询当前实际个数
#macro($indexDataCount())
SELECT count(1) FROM exam_paper WHERE status != 5
#end