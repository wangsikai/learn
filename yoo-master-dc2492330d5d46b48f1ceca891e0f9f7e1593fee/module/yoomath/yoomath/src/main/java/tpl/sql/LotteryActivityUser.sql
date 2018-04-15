#macro($getLotteryActivityUser(activityCode, userId))
SELECT * FROM lottery_activity_user WHERE activity_code=:activityCode AND user_id=:userId
#end