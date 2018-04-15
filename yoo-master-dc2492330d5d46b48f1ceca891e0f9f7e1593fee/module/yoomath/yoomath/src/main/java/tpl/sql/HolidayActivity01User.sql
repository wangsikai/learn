## 获得用户信息
#macro($findByUserId(code, userId))
SELECT * FROM holiday_activity_01_user WHERE activity_code = :code and user_id = :userId
#end

## 重置用户新增抽奖次数
#macro($resetUserNewLuckyDraw(activityCode, userId))
update holiday_activity_01_user set new_lucky_draw=0 where user_id=:userId and activity_code=:activityCode
#end

## 用户抽奖次数的更新
#macro($addUserLuckyDraw(activityCode, userId, num, isCost, isNew))
update holiday_activity_01_user set 
#if(isCost == false)
 lucky_draw=lucky_draw+:num
#end
#if(isCost == true)
 cost_lucky_draw=cost_lucky_draw-:num
#end
#if(isNew == true)
 ,new_lucky_draw=new_lucky_draw+:num
#end
 where user_id=:userId and activity_code=:activityCode
#end