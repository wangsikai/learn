## 分页查找教辅数据
#macro($indexFind())
SELECT t.* FROM teachassist t WHERE t.del_status = 0
#end

## 查询教辅数据个数
#macro($indexDataCount())
SELECT count(id) FROM teachassist t WHERE t.del_status = 0
#end