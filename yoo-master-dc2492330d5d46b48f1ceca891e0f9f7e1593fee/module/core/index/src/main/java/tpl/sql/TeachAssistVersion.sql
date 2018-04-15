## 查找教辅的版本
#macro($findByTeachAssist(ids))
SELECT t.* FROM teachassist_version t WHERE t.teachassist_id IN :ids AND t.del_status = 0
#end