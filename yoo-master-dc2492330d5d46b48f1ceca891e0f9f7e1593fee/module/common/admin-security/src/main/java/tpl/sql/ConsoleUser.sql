#macro($getByName(name, systemId))
SELECT * FROM con_user t WHERE t.name = :name AND (t.system_id = 0 or t.system_id = :systemId)
#end

#macro($getByGroup(groupId))
SELECT t.* FROM con_user t, con_user_group g WHERE t.id = g.user_id AND g.group_id = :groupId
#end

## 分页查询
#macro($query(systemId, name))
SELECT t.* FROM con_user t WHERE 1=1
#if(systemId)
AND t.system_id = :systemId
#end
#if(name)
AND t.name LIKE :name
#end
ORDER BY t.system_id ASC
#end

## 查找一个系统下的所有用户
#macro($findBySystem(systemId))
SELECT u.* FROM con_user u WHERE u.system_id = :systemId AND u.status = 0
#end