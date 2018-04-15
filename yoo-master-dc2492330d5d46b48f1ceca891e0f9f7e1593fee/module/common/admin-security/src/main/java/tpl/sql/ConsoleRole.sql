## 分页查询角色数据
#macro($query(name))
SELECT t.* FROM con_role t WHERE t.status = 0
#if(name)
AND t.name LIKE :name
#end
#end

## 查询用户所具有的所有角色权限
#macro($queryUserRole(userId))
select distinct m.* from con_role m
where m.id in
(
	select rg.role_id from con_role_group rg, con_group g, con_user_group ug, con_role cr
    where rg.group_id = g.id and g.id = ug.group_id and ug.user_id = :userId  AND cr.id = rg.role_id AND cr.status = 0
    union
    select ru.role_id from con_user_role ru, con_user u, con_role cr
    where ru.user_id = u.id and u.id = :userId AND cr.status = 0 AND cr.id = ru.role_id
)
and m.status = 0
#end
