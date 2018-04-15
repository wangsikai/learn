##根据题目获取考点集合
#macro($indexFindExaminationPointByQuestion(questionId))
SELECT k.* from question_examination_point t inner join examination_point k on k.id = t.examination_point_code
 where t.question_id =:questionId and t.status = 0
#end

##根据题目获取题目考点关联
#macro(indexListByQuestions(questionIds))
SELECT t.* from question_examination_point t where t.question_id in (:questionIds) and t.status = 0
#end