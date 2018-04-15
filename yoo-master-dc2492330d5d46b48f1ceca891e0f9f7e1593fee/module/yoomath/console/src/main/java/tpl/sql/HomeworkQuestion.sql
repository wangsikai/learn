#macro($zycGetQuestion(homeworkId))
SELECT question_id FROM homework_question WHERE status = 0 AND homework_id = :homeworkId ORDER BY sequence ASC
#end