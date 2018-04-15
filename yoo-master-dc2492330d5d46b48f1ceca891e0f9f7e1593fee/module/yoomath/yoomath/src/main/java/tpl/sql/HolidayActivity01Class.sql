## 获得用户信息
#macro($findClassByUserId(userId,activityCode))
SELECT * FROM holiday_activity_01_class 
WHERE user_id = :userId
#if(activityCode)
	AND activity_code = :activityCode
#end
#end