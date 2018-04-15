#macro($getBySystem(systemId, status))
SELECT * FROM con_menu t WHERE
t.system_id = :systemId AND t.status < 2
#if(status)
AND t.status = :status
#end
#end

#macro($getByUser(userId, systemId))
select distinct m.* from con_menu m, con_role_menu rm
where m.id = rm.menu_id and rm.role_id in
(
	select rg.role_id from con_role_group rg, con_group g, con_user_group ug, con_role cr
    where rg.group_id = g.id and g.id = ug.group_id and ug.user_id = :userId and g.system_id = :systemId AND cr.id = rg.role_id AND cr.status = 0
    union
    select ru.role_id from con_user_role ru, con_user u, con_role cr
    where ru.user_id = u.id and u.id = :userId and u.system_id = :systemId AND cr.status = 0 AND cr.id = ru.role_id
)
and m.status = 0
#end

## 更新菜单状态
#macro($updateStatus(ids, status))
UPDATE con_menu SET status = :status WHERE id IN :ids
#end
