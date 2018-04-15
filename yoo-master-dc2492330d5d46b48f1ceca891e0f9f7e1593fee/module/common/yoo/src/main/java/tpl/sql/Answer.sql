## 查找题目答案
#macro($getQuestionAnswers(qid,qids))
SELECT * FROM answer WHERE 1=1
#if(qid)
AND question_id = :qid
#end
#if(qids)
AND question_id IN :qids
#end
ORDER BY sequence ASC
#end