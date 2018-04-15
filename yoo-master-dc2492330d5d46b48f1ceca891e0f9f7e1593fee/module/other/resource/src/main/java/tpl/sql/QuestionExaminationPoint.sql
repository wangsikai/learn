##查询新考点关联的题目集合
#macro($findQuestionIds(examIds,vendorId))
SELECT distinct a.question_id FROM question_examination_point a
inner join question b on a.question_id = b.id and b.vendor_id=:vendorId
WHERE a.examination_point_code in (:examIds) and a.status = 0
#end

##根据题目获取考点集合
#macro($findExaminationPointByQuestion(questionId))
SELECT k.* from question_examination_point t inner join examination_point k on k.id = t.examination_point_code
 where t.question_id =:questionId and t.status = 0
#end

##根据题目获取题目考点关联
#macro(listByQuestions(questionIds))
SELECT t.* from question_examination_point t where t.question_id in (:questionIds) and t.status = 0
#end

##根据考点获取所有题目集合
#macro($findAllQuestionByExaminationPoint(code))
SELECT t.* from question_examination_point t where t.examination_point_code=:code
#end
