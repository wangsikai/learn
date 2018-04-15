#macro($indexListByQuestion(questionId))
select t.tag_code from question_2_tag t 
inner join question_tag qt on qt.code=t.tag_code and qt.status=0
where t.question_id=:questionId
order by qt.sequence ASC
#end

#macro($indexListByQuestions(questionIds))
select t.* from question_2_tag t 
inner join question_tag qt on qt.code=t.tag_code and qt.status=0
where t.question_id in (:questionIds)
order by qt.sequence ASC
#end