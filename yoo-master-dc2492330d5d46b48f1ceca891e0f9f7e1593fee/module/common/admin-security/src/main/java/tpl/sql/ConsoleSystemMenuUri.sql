## 按系统查询菜单
#macro($queryBySystem(systemId))
	select * from con_system_menu_uri where system_id =:systemId
#end