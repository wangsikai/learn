##查询小优快批传题记录
#macro($queryCorrectQuestionFailRecords())
select * from correct_question_fail_record r where r.status=0
#end

##逻辑删除
#macro($deleteRecords(ids,successAt))
update correct_question_fail_record r set r.status=2,r.success_at=:successAt where r.id in (:ids)
#end