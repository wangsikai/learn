##根据针对性训练查询对应题目集合
#macro($queryQuestionList(specialTrainingId))
SELECT a.* FROM special_training_question a INNER JOIN question b ON a.question_id = b.id AND b.del_status = 0
WHERE a.special_training_id = :specialTrainingId ORDER BY a.sequence
#end

##根据针对性训练ID删除题目
#macro($deleteQuestions(specialTrainingId))
DELETE FROM special_training_question WHERE special_training_id = :specialTrainingId
#end

##根据针对性训练id和题目ID删除题目
#macro($deleteQuestion(specialTrainingId,questionId))
DELETE FROM special_training_question WHERE special_training_id = :specialTrainingId and question_id =:questionId
#end

##训练题目的不同状态的统计
#macro($getQuestionStat(specialTrainingId))
SELECT COUNT(1) count,b.status FROM special_training_question a 
INNER JOIN question b ON a.question_id = b.id AND b.del_status = 0
WHERE special_training_id = :specialTrainingId GROUP BY b.status
#end


## 替换习题
#macro($changeQuestion(trainId, oldQuestionId, newQuestionId))
update special_training_question set question_id=:newQuestionId 
 where special_training_id=:trainId and question_id=:oldQuestionId
#end