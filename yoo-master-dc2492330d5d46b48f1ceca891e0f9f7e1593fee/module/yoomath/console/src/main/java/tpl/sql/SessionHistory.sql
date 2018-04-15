#macro($getSessionHisList(loginAtBegin,loginAtEnd,activeAtBegin,activeAtEnd,accountName,type))
select sh.* from session_history sh 
#if(accountName)
inner join account a on sh.account_id = a.id and a.name like :accountName 
#end
where sh.user_id !=1 and (sh.user_type=1 or sh.user_type=2) and sh.account_id !=1
#if(loginAtBegin)
AND sh.login_at >=:loginAtBegin
#end
#if(loginAtEnd)
AND sh.login_at <=:loginAtEnd
#end
#if(activeAtBegin)
AND sh.active_at >=:activeAtBegin
#end
#if(activeAtEnd)
AND sh.active_at <=:activeAtEnd
#end
#if(type)
AND sh.device_type =:type
#end
order by sh.create_at desc
#end




