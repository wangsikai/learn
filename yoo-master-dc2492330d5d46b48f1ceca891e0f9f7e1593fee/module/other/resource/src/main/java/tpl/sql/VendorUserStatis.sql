## 统计
#macro($resconGetAllDetail(vendorId,userType,userId))
SELECT 
	t1.id AS t1userid,t1.type AS t1type,t1.name AS t1name,t1.real_name AS t1realname,t2.* 
FROM vendor_user t1 INNER JOIN vendor_user_statis t2 ON t1.id = t2.user_id
WHERE t2.type = 0 
#if(vendorId)
AND t1.vendor_id = :vendorId
#end
#if(userType)
AND t1.type = :userType
#end
#if(userId)
AND t1.id = :userId
#end
#end

## 按时间统计
#macro($resconQueryDayDetail(vendorId,userType,startTime,endTime,userId))
SELECT 
t1.id AS t1userid,t1.type AS t1type,t1.name AS t1name,t1.real_name AS t1realname,t2.* 
FROM vendor_user t1 INNER JOIN
(
SELECT 
	user_id,
	SUM(build_q_count) build_q_count,
	SUM(pass_step1_q_count) pass_step1_q_count,
	SUM(pass_q_count) pass_q_count,
##	SUM(checking_q_count) checking_q_count,
##  SUM(nocheck_q_count) nocheck_q_count,
	SUM(nopass_q_count) nopass_q_count,
	SUM(draft_q_count) draft_q_count,
	SUM(build_modify_count) build_modify_count,
	SUM(check_check_count) check_check_count,
	SUM(check_modify_count) check_modify_count,
	SUM(check_check1_count) check_check1_count,
	SUM(check_check2_count) check_check2_count
FROM vendor_user_statis 
WHERE TYPE = 1
#if(startTime)
	AND create_at >= :startTime
#end
#if(endTime)
	AND create_at <= :endTime  
#end
#if(userId)
	AND user_id = :userId
#end
GROUP BY user_id
) t2 ON t1.id = t2.user_id WHERE 1=1
#if(vendorId)
AND t1.vendor_id = :vendorId
#end
#if(userType)
AND t1.type = :userType
#end
#end

## 获取统计
#macro($resconGet(userId,type,dayDetail))
SELECT * FROM vendor_user_statis 
WHERE user_id = :userId AND type = :type
#if(dayDetail)
 AND to_days(create_at) = to_days(:dayDetail);
#end
#end