##通过题目ID查询对应章节和教材信息
#macro($zycFindByQuestionId(questionId))
SELECT * from question_section q where q.question_id =:questionId
#end