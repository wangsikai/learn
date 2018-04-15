#macro($deleteRoleMenu(roleId,menuIds))
DELETE FROM con_role_menu WHERE role_id = :roleId AND menu_id in :menuIds
#end

#macro($getMenuByRole(roleId))
SELECT * FROM con_role_menu t WHERE t.role_id = :roleId
#end

## 查询一个角色及系统下的菜单
#macro($getRoleSystemMenus(roleId,systemId))
SELECT t.id FROM con_role_menu c INNER JOIN con_menu t ON t.id = c.menu_id
WHERE c.role_id = :roleId AND t.system_id = :systemId
#end

## 根据角色及菜单id查找数据
#macro($findByRoleMenus(roleId,menuIds))
SELECT t.* FROM con_role_menu t WHERE t.role_id = :roleId AND t.menu_id IN :menuIds
#end
