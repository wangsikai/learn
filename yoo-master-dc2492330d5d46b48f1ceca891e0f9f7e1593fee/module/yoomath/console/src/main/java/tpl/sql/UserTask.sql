#macro($zycUserTaskList(type))
select t.* from user_task t where 1=1
#if(type)
and t.type=:type
#end
ORDER BY t.sequence ASC
#end

