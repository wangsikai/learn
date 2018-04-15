
##分配待批改题目给批改人员
#macro($distributeCorrectQuestionToUser(correctQuestionId,correctUserId,firstAllotAt,allotAt,correctUserIds))
UPDATE correct_question cq set cq.correct_user_id = :correctUserId,cq.allot_at = :allotAt
#if(firstAllotAt)
,cq.first_allot_at = :firstAllotAt
#end
#if(correctUserIds)
,cq.correct_user_ids = :correctUserIds
#end
where cq.id = :correctQuestionId and cq.status=0
#end

##将题目退回到待批改池
#macro($sendBackCorrectQuestions(correctQuestionIds))
update correct_question cq set cq.correct_user_id = null where cq.id in(:correctQuestionIds) and cq.complete_at is null
#end

##查询
#macro($queryCompleteQuestionsByUserId(userId,datefrom,dateEnd))
SELECT * FROM correct_question
WHERE correct_user_id = :userId
AND complete_at IS NOT NULL
#if(datefrom)
AND complete_at >= :datefrom
#end
#if(dateEnd)
AND complete_at < :dateEnd
#end
#end

##查询普通用户待批改习题
#macro($queryCorrectQuestionsToTeacher(size, exqids))
SELECT * FROM correct_question where status = 0 and complete_at is null and correct_user_id is null and is_error=0
 and (question_category = 0 or question_category = 1) 
 #if(exqids)
 and id not in (:exqids)
 #end
 order by TRUNCATE(weight, 1) DESC,create_at ASC limit :size
#end

##查询管理员用户待批改习题
#macro($queryCorrectQuestionsToAdmin(size))
SELECT * FROM correct_question where status = 0 and complete_at is null and correct_user_id is null
 order by TRUNCATE(weight, 1) DESC,create_at ASC limit :size
#end

##按权重降序查询待批改提
#macro($queryCorrectQuestions())
SELECT * FROM correct_question where status = 0 and complete_at is null and correct_user_id is null
 order by TRUNCATE(weight, 1) DESC,create_at ASC 
#end

##查询待批改习题池中异常的数据
#macro($queryAbnormalCorrectQuestions())
SELECT * FROM correct_question where status = 0 and right_rate is null and complete_at is null and correct_user_id is not null
#end

##让异常习题重新派发
#macro($clearAbnormalCorrectQuestions(correctQuestionIds))
update correct_question set correct_user_id = null where id IN(:correctQuestionIds)
#end
