## 获取奖品
#macro($findPrizes(code,process,room,userType))
SELECT * FROM imperial_exam_activity_prizes 
WHERE activity_code=:code 
AND process = :process
#if(room)
 AND room = :room
#end
AND user_type = :userType
AND num > cost
#end

## 更新奖品数量
#macro($updatePrizesCost(id))
UPDATE imperial_exam_activity_prizes
SET cost = cost + 1
WHERE id = :id
AND num > cost
#end