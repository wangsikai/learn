## 题目标签对应关系
#macro($getByQuestionId(questionId))
SELECT * FROM question_2_tag
WHERE 
	question_id = :questionId
#end

## 题目标签对应关系
#macro($mgetByQuestionIds(questionIds))
SELECT * FROM question_2_tag
WHERE 
	question_id in (:questionIds)
#end
