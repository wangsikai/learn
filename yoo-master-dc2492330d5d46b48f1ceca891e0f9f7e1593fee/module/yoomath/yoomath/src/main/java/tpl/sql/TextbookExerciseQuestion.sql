## 获取某个老师待处理的作业列表
#macro($zyListQuestions(textbookExerciseId))
SELECT * FROM textbook_exercise_question WHERE exercise_id = :textbookExerciseId ORDER BY sequence ASC,id ASC
#end