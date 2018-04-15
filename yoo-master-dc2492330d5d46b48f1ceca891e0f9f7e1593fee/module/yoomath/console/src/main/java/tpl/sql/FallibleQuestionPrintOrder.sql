##查询代打印列表
#macro($queryPrintList(startAt,endAt,status,accountName))
	select * from fallible_question_print_order a
	#if(accountName)
		inner join user b on a.user_id = b.id
		inner join account c on b.account_id = c.id
	#end
	where a.status in (1,2,3,4)
	#if(startAt)
		and a.pay_time >= :startAt
	#end
	#if(endAt)
		and a.pay_time <= :endAt
	#end
	#if(status)
		and a.status = :status
	#end
	#if(accountName)
		and c.name like :accountName
	#end
	order by a.pay_time desc
#end

##查询代打印共有多少条记录
#macro($countPrintList(status))
	select count(id) from fallible_question_print_order 
	where status in (1,2,3,4)
	#if(status)
		and status = :status
	#end
#end
