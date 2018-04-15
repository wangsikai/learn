#macro($deleteUserRole(userId, roleId))
DELETE FROM con_user_role WHERE user_id = :userId AND role_id = :roleId
#end

## 根据用户及角色查找对应关系
#macro($findByUserAndRole(userId, roleId))
SELECT * FROM con_user_role WHERE user_id = :userId AND role_id = :roleId
#end

## 查询角色下的所有用户id
#macro($findByRole(roleId))
SELECT t.user_id FROM con_user_role t WHERE t.role_id = :roleId
#end