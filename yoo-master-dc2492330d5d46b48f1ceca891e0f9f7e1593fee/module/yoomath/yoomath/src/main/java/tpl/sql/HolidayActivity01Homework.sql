## 根据用户获得全部信息
#macro($findHolidayActivity01ClassUser(userId, activityCode))
SELECT * FROM holiday_activity_01_homework WHERE user_id = :userId
AND activity_code = :activityCode
#end

## 判断用户当前活动有没有布置过作业
#macro($countHk(userId,code))
SELECT count(id) FROM holiday_activity_01_homework WHERE user_id = :userId and activity_code = :code
#end

## 查询某个老师下同一开始时间的作业数量
#macro($countSameStartTimeHomework(activityCode,userId,startAt))
SELECT 
	COUNT(id) 
FROM holiday_activity_01_homework 
WHERE 
	user_id = :userId 
AND activity_code = :activityCode
AND DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%S') = :startAt
#end