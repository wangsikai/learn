## 查询用户参加比赛的信息
#macro($findByUserId(code, userId))
SELECT * FROM holiday_activity_02_user WHERE activity_code = :code and user_id = :userId
#end


## 查询当前活动最大的活动编号
#macro($getMaxUserCode(code))
SELECT MAX(activity_user_code) FROM holiday_activity_02_user WHERE activity_code = :code
#end

## 查询当前活动最大的活动编号
#macro($updateUserPower(code, userId, difference))
UPDATE holiday_activity_02_user
SET power = power + :difference
WHERE activity_code = :code
AND user_id = :userId
#end