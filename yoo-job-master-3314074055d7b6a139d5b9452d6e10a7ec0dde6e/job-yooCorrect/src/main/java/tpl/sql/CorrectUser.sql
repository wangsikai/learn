##根据用户类型查询等待中的批改人员
#macro($queryWaitToCoorecUsersByType(correctUserType))
SELECT cu.* FROM correct_user_pool cup INNER JOIN correct_user cu ON cup.user_id = cu.id
#if(correctUserType)
AND cu.user_type = :correctUserType
#end
AND cu.status=0 AND cup.status=0 AND cup.correct_question_id is null
order by cu.trust_rank desc
#end

#macro($taskQueryUser())
SELECT id FROM correct_user 
WHERE qualification_auth_status = 2
AND id < :next
ORDER BY id DESC
#end

#macro($findAllCorrectUserIds())
select u.id from correct_user u where u.status=0
#end