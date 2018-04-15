## 查找多个题目对应的考点
#macro($findByQuestions(questionIds))
SELECT t.* FROM question_examination_point t WHERE t.question_id IN :questionIds
#end

## 查找一个题目的考点
#macro($findByQuestion(questionId))
SELECT t.* FROM question_examination_point t WHERE t.question_id = :questionId
#end