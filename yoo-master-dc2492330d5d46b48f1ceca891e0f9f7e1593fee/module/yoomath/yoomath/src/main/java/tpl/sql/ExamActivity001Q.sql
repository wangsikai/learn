## 获取奖品
#macro($ymFindPrizes(code,day))
SELECT * FROM exam_activity_001_q 
WHERE activity_code=:code 
AND day0 =:day
AND num > cost
#end

## 更新奖品数量
#macro($ymUpdatePrizesCost(id))
UPDATE exam_activity_001_q
SET cost = cost + 1
WHERE id = :id
AND num > cost
#end