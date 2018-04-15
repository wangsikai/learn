##分配题目给批改池中的用户
#macro($distributeCorrectQuestionToUser(correctQuestionId,correctUserId,startAt,questionType))
update correct_user_pool pool set pool.correct_question_id = :correctQuestionId,pool.start_at=:startAt
,pool.question_type=:questionType
where pool.user_id = :correctUserId
#end

##删除批改池用户
#macro($updateCorrectUserPoolToDisable(correctUserId))
update correct_user_pool pool set pool.correct_question_id = null,pool.start_at=null,pool.status=2
where pool.user_id = :correctUserId
#end

##查询已经被分配了批改题目的批改用户
#macro($findHasDistributedUsers())
select * from correct_user_pool pool where pool.correct_question_id is not null
#end

#macro($deleteTimeOutCorrectUserPools(correctUserPoolIds))
update correct_user_pool p set p.correct_question_id = null,p.start_at=null,p.status=2
where p.id in(:correctUserPoolIds)
#end