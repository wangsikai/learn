## 获得指定活动的用户抽奖信息
#macro($findUserLuckyByUserId(code, userId, nowdate))
SELECT * FROM holiday_activity_01_user_luckydraw 
WHERE activity_code = :code 
and user_id = :userId
#if(nowdate)
	AND DATE_FORMAT(create_at,'%Y-%m-%d') = :nowdate
#end
#end

## 获得指定活动的用户抽奖信息
#macro($findLuckyDrawByCode(code, nowdate))
SELECT * FROM holiday_activity_01_user_luckydraw 
WHERE activity_code = :code 
#if(nowdate)
	AND DATE_FORMAT(create_at,'%Y-%m-%d') = :nowdate
#end
#end

## 获得指定活动的用户抽奖统计信息
#macro($findUserLuckyCountByUserId(code, userId, nowdate))
SELECT count(*) FROM holiday_activity_01_user_luckydraw 
WHERE activity_code = :code 
and user_id = :userId
#if(nowdate)
	AND DATE_FORMAT(create_at,'%Y-%m-%d') = :nowdate
#end
#end