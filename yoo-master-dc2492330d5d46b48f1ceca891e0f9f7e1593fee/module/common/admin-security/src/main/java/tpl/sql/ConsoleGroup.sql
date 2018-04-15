## 分页查询用户组
#macro($query(systemId,name))
SELECT t.* FROM con_group t WHERE 1=1
#if(systemId)
AND t.system_id = :systemId
#end
#if(name)
AND t.name LIKE :name
#end
ORDER BY t.system_id ASC
#end