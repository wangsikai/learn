## 获取获奖排名
#macro($findEffectiveList(code))
SELECT * FROM imperial_exam_activity_award WHERE status=0 and activity_code=:code order by rank ASC,id ASC
#end

## 获取用户获奖排名
#macro($getByUser(code, userId))
SELECT * FROM imperial_exam_activity_award WHERE activity_code=:code and user_id=:userId
#end

## 获取获奖排名
#macro($findEffectiveListByRoom(code,room))
SELECT * FROM imperial_exam_activity_award 
WHERE status=0 
AND activity_code=:code 
AND room = :room
AND rank IS NOT NULL
ORDER BY rank ASC,id ASC
#end