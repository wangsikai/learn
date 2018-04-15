#macro($getSessionList(loginAtBegin,loginAtEnd,activeAtBegin,activeAtEnd,accountName,status,type))
select s.* from session s 
#if(accountName)
inner join account a on s.account_id = a.id and a.name like :accountName
#end
where 1=1
#if(loginAtBegin)
AND s.login_at >=:loginAtBegin
#end
#if(loginAtEnd)
AND s.login_at <=:loginAtEnd
#end
#if(activeAtBegin)
AND s.active_at >=:activeAtBegin
#end
#if(activeAtEnd)
AND s.active_at <=:activeAtEnd
#end
#if(type)
AND s.device_type =:type
#end
#if(status)
AND s.status =:status
#end
AND s.user_id !=1 and (s.user_type=1 or s.user_type=2)
#end

#macro($getSessionListByUserId(activeAtEnd,userIds,status))
select s.* from session s 
#if(accountName)
inner join account a on s.account_id = a.id and a.name like :accountName
#end
where 1=1
#if(activeAtEnd)
AND s.active_at >=:activeAtEnd
#end
#if(status)
AND s.status =:status
#end
AND s.user_id IN (:userIds)
GROUP BY s.user_id
#end


