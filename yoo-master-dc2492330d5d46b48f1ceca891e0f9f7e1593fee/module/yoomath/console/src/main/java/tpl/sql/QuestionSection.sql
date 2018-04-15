#macro($zycFindByQuestionIds(questionIds))
SELECT * from question_section q where q.question_id in :questionIds
#end