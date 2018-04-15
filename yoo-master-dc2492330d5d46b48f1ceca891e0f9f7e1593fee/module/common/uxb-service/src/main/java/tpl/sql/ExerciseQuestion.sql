#macro($getExerciseQuestion(exerciseId,questionId))
SELECT * FROM exercise_question WHERE status = 0 
AND exercise_id = :exerciseId 
#if(questionId)
AND question_id = :questionId
#end
ORDER BY sequence ASC
#end

#macro($getQuestion(exerciseId))
SELECT question_id FROM exercise_question WHERE status = 0 AND exercise_id = :exerciseId ORDER BY sequence ASC
#end

#macro($getMaxSequence(exerciseId))
SELECT MAX(sequence) FROM exercise_question WHERE status = 0 AND exercise_id = :exerciseId
#end

#macro($delQuestion(exerciseId,questionId))
DELETE FROM exercise_question WHERE exercise_id = :exerciseId AND question_id = :questionId
#end