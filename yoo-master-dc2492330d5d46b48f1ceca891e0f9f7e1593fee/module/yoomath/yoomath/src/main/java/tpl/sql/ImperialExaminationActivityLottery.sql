## 获取用户奖券
#macro($findLotteryByUser(code,process,userId,status))
SELECT * FROM imperial_exam_activity_lottery 
WHERE activity_code=:code 
AND user_id = :userId
AND process = :process
#if(status)
AND status = :status
#end
#end