#macro($TaskGetMaxSequence(exerciseId))
SELECT MAX(sequence) FROM exercise_question WHERE status = 0 AND exercise_id = :exerciseId
#end

#macro($TaskGetQuestion(exerciseId))
SELECT question_id FROM exercise_question WHERE status = 0 AND exercise_id = :exerciseId ORDER BY sequence ASC
#end

