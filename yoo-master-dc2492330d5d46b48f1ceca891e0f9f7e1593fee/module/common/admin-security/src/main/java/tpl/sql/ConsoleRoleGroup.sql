#macro($deleteGroupRole(groupId, roleId))
DELETE FROM con_role_group WHERE group_id = :groupId AND role_id = :roleId
#end

## 根据用户组及角色查询
#macro($findByGroupAndRole(groupId, roleId))
SELECT t.* FROM con_role_group t WHERE group_id = :groupId AND role_id = :roleId
#end

## 查询角色授权过的用户组id列表
#macro($findByRole(roleId))
SELECT t.group_id FROM con_role_group t WHERE t.role_id = :roleId
#end