## 查询用户达成但未领取的勋章
#macro($findNotReceivedMedals(code, userId))
SELECT * FROM holiday_activity_02_medal WHERE activity_code = :code and user_id = :userId
and achieved=1 and received=0
#end


## 查询当前用户的所有勋章
#macro($findAllMedals(code,userId))
SELECT * FROM holiday_activity_02_medal 
WHERE activity_code = :code 
and user_id = :userId
order by level ASC
#end


## 查询用户未达成的勋章
#macro($findNotAchievedMedals(code, userId))
SELECT * FROM holiday_activity_02_medal WHERE activity_code = :code and user_id = :userId
and achieved=0
#end


