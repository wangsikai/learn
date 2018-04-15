## 获取获奖排名
#macro($findEffectiveList(code))
SELECT * FROM imperial_exam_activity_award_student
WHERE status=0 and activity_code=:code order by rank ASC,id ASC
#end

## 获取用户获奖排名
#macro($getByUser(code, userId))
SELECT * FROM imperial_exam_activity_award_student WHERE activity_code=:code and user_id=:userId
#end