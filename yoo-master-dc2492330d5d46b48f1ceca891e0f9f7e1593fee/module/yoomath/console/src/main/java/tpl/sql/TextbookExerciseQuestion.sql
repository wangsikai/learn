## 获取作业列表
#macro($zycListQuestions(textbookExerciseId))
SELECT * FROM textbook_exercise_question WHERE exercise_id = :textbookExerciseId ORDER BY sequence ASC,id ASC
#end

##清除原来的exercise下面的题目以便重新预置
#macro($clearExercise(exerciseId))
delete from textbook_exercise_question where exercise_id=:exerciseId
#end