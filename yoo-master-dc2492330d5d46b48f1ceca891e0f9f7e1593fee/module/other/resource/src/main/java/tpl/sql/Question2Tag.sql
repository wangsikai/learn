#macro($deleteManualByQuestion(questionId))
delete from question_2_tag where question_id=:questionId and system=0
#end

#macro($listByQuestion(questionId))
select t.* from question_2_tag t 
inner join question_tag qt on qt.code=t.tag_code and qt.status=0
where t.question_id=:questionId
order by qt.sequence ASC
#end

#macro($listByQuestions(questionIds))
select t.* from question_2_tag t 
inner join question_tag qt on qt.code=t.tag_code and qt.status=0
where t.question_id in (:questionIds)
order by qt.sequence ASC
#end

## 根据习题与标签寻找关系
#macro($findByQuestionAndTag(questionIds, tagCode))
select t.* from question_2_tag t where t.question_id in (:questionIds) and t.tag_code=:tagCode
#end