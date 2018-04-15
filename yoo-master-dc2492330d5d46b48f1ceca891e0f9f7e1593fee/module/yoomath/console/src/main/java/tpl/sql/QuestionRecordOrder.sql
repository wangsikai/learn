## 查询请求数据列表
#macro($zycQuery(accountName,schoolName,status,startTime,endTime))
SELECT t.* FROM question_record_order t
INNER JOIN user u ON u.id = t.user_id
INNER JOIN account a ON a.id = u.account_id
#if(schoolName)
INNER JOIN teacher te ON te.id = t.user_id
INNER JOIN school s ON te.school_id = s.id
#end
WHERE
1=1
#if(accountName)
AND a.name LIKE :accountName
#end
#if(schoolName)
AND s.name LIKE :schoolName
#end
#if(status)
AND t.order_status = :status
#end
#if(startTime)
AND t.order_at >= :startTime
#end
#if(endTime)
AND t.order_at <= :endTime
#end
ORDER BY t.order_at DESC
#end

## count待处理数据
#macro($zycCountDo())
SELECT count(*) FROM question_record_order t WHERE t.order_status IN (0, 3) AND t.del_status = 0
#end