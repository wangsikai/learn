## 查询兑换卡
#macro($csQueryMemberPackageCard(code,memo,userType,memberType,month,status,createId,startAt,endAt,orderType,createDate,codes,noCodes,curdate))
select * from (SELECT
	t2.`code`,
	t2.create_id,
	t2.create_time,
	t2.encrypt_code,
	t2.end_at,
	t2.member_type,
	t2.memo,
	t2.month0,
	t2.order_id,
	t2.price,
	t2.update_id,
	t2.update_time,
	t2.used_time,
	t2.user_id,
	t2.user_type,
	CASE
WHEN t2.`status` = 0 AND t2.end_at < :curdate THEN 1 ELSE t2.`status` END as `status` FROM member_package_card t2) t
WHERE 1=1
#if(code)
 and t.code =:code
#end
#if(memo)
 and  t.memo like :memo
#end
#if(userType)
 and t.user_type =:userType
#end
#if(memberType)
 and t.member_type =:memberType
#end
#if(month)
 and t.month0=:month
#end
#if(status)
 and t.status =:status
#end
#if(createId)
 and t.create_id=:createId
#end
#if(createDate)
  and t.create_time=:createDate
#end
#if(startAt)
	AND t.create_time >= :startAt
#end
#if(endAt)
	AND t.create_time < :endAt
#end
#if(codes)
   and t.code in (:codes)
#end
#if(noCodes)
   and t.code not in (:noCodes)
#end
ORDER BY t.status ASC 
#if(orderType==0)
 ,t.create_time DESC,t.end_at ASC
#else
 ,t.end_at ASC,t.create_time DESC
 #end
#end




