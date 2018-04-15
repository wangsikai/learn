#macro($deleteByQuestion(questionId))
delete from question_2_category where question_id=:questionId
#end

#macro($listByQuestion(questionId))
select qc.* from question_2_category t 
inner join question_category qc on qc.code=t.category_code and qc.status=0
where t.question_id=:questionId
order by qc.code ASC
#end

#macro($listByQuestions(questionIds))
select t.* from question_2_category t 
inner join question_category qc on qc.code=t.category_code and qc.status=0
where t.question_id in (:questionIds)
order by qc.code ASC
#end
