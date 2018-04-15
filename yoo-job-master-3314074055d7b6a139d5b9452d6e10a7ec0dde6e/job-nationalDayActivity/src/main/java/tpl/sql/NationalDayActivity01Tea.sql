#macro($findAllTeaByCursor())
SELECT *
FROM national_day_activity_01_tea
WHERE id < :next 
ORDER BY id,score DESC
#end

#macro($findDeteleUserIds())
select * from national_day_activity_01_tea tea WHERE NOT EXISTS 
(
select h.teacher_id from national_day_activity_01_homework h where tea.user_id = h.teacher_id
)
#end

#macro($taskFindTopN(topn))
SELECT *
FROM national_day_activity_01_tea
ORDER BY score DESC, homework_count DESC, commit_rate DESC, user_id ASC 
LIMIT :topn
#end
