#macro($queryErrorQuestionCount())
SELECT COUNT(1) COUNT,a.question_id,b.code,c.name typename,d.name phasename FROM  question_error a 
INNER JOIN question b ON a.question_id = b.id
INNER JOIN QUESTION_TYPE c ON b.type_code = c.code 
INNER JOIN PHASE d ON b.phase_code = d.code
and a.status = 0
 GROUP BY a.question_id
 ORDER BY a.create_at desc
#end

#macro($queryError(questionId))
SELECT a.*,b.name from question_error a INNER JOIN USER b 
ON a.user_id = b.id
where a.question_id = :questionId
and a.status = 0
#end

#macro($mQueryError(questionIds))
SELECT a.*,b.name from question_error a INNER JOIN USER b 
ON a.user_id = b.id
where question_id in (:questionIds)
and a.status = 0
#end


#macro($getlatestQuestionError())
SELECT question_id FROM question_error WHERE id = (SELECT MAX(id) FROM question_error WHERE STATUS = 0)
#end

#macro($queryOneErrorQuestionCount(questionId))
SELECT COUNT(1) COUNT,a.question_id,b.code,c.name typename,d.name phasename FROM  question_error a 
INNER JOIN question b ON a.question_id = b.id
INNER JOIN QUESTION_TYPE c ON b.type_code = c.code 
INNER JOIN PHASE d ON b.phase_code = d.code
and a.status = 0 and a.question_id = :questionId
 ORDER BY a.create_at desc
#end