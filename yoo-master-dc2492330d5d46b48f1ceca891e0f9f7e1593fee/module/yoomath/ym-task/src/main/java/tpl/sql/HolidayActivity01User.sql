## 用户抽奖次数的更新
#macro($taskAddUserLuckyDraw(activityCode, userId, num, isCost, isNew))
update holiday_activity_01_user set lucky_draw=lucky_draw+:num
#if(isCost == true)
 ,cost_lucky_draw=cost_lucky_draw-:num
#end
#if(isNew == true)
 ,new_lucky_draw=new_lucky_draw+:num
#end
 where user_id=:userId and activity_code=:activityCode
#end