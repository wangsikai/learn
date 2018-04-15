##查询专项题目对应的答案
#macro($queryItemAnswers(itemQuestionId,itemQuestionIds))
SELECT * FROM holiday_stu_homework_item_answer 
WHERE 1=1
	#if(itemQuestionId)
	AND holiday_stu_homework_item_qid = :itemQuestionId
	#end
	#if(itemQuestionIds)
	AND holiday_stu_homework_item_qid in (:itemQuestionIds)
	#end
ORDER BY sequence
#end

## 删除题目答案
#macro($deleteItemAnswers(itemQuestionId))
DELETE FROM holiday_stu_homework_item_answer WHERE holiday_stu_homework_item_qid = :itemQuestionId
#end

## 更新答案
#macro($updateItemAnswer(itemQuestionId,content,asciiContent,sequence,sovlingImg,answerAt,answerId))
UPDATE holiday_stu_homework_item_answer SET content = :content, content_ascii = :asciiContent,
answer_at = :answerAt, answer_id = :answerId
WHERE holiday_stu_homework_item_qid = :itemQuestionId AND sequence = :sequence
#end

