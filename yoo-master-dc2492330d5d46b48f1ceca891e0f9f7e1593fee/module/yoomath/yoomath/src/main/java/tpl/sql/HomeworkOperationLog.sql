## 查询log是否存在
#macro($zyExist(type,hkId))
SELECT count(id) FROM homework_operation_log WHERE type = :type AND homework_id = :hkId
#end