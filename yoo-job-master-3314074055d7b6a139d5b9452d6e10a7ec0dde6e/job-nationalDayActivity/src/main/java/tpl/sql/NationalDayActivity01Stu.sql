#macro($nda01IncrRightCount(userId,rightCount))
UPDATE national_day_activity_01_stu
SET right_count = right_count + :rightCount
WHERE user_id = :userId
#end

#macro($ndaAwardList())
SELECT * FROM national_day_activity_01_stu ORDER BY right_count DESC,user_id ASC LIMIT 50
#end