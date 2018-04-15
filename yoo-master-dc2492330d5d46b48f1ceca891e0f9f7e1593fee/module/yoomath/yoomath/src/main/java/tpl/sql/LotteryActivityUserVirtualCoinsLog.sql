#macro($findNewCount(activityCode, lastTime, userId))
SELECT sum(increase_coins) FROM lottery_activity_user_virtual_coins WHERE activity_code=:activityCode AND user_id=:userId
#if(lastTime)
 AND create_at>:lastTime
#end
#end