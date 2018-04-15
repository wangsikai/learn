#macro($nda01PullHomework(maxHkId, minStartAt, maxStartAt, minDistributeCount))
SELECT * FROM homework 
WHERE 
	id <= :maxHkId 
AND start_time >= :minStartAt 
AND start_time <= :maxStartAt 
AND distribute_count >= :minDistributeCount
AND status > 0
AND del_status = 0
#end

#macro($nda01QueryRightQuestion(homeworkId))
SELECT
	stuhk.student_id,
	stuhkq.question_id
FROM
	homework hk
INNER JOIN student_homework stuhk ON hk.id = stuhk.homework_id
AND hk.id = :homeworkId
INNER JOIN student_homework_question stuhkq ON stuhk.id = stuhkq.student_homework_id
AND stuhkq.result = 1
#end