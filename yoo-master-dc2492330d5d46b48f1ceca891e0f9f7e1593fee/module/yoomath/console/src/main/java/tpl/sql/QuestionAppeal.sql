##查询题目申述的记录
#macro($zycFindQuestionAppeal(startTime,endTime,accountName,schoolName,correctName,status))
SELECT qa.create_at as createAt,a.name as accountName,u.name as realName,sl.name as schoolName,qa.comment,qa.creator,
qa.question_source as source,hk.name as homeworkName,qa.status,qa.biz_id as stuHkQId,IFNULL(tc.name,cu.name) as correctName
from question_appeal qa
INNER JOIN user u ON u.id =qa.creator and u.status=0
INNER JOIN account a on a.id=u.account_id
LEFT JOIN student s ON s.id =u.id and qa.user_type = 2
LEFT JOIN teacher t ON t.id = u.id and qa.user_type = 1
LEFT JOIN school sl on s.school_id =sl.id or t.school_id = sl.id
INNER JOIN student_homework_question shq on shq.id = qa.biz_id 
INNER JOIN student_homework sh on sh.id = shq.student_homework_id 
INNER JOIN homework hk on hk.id = sh.homework_id
LEFT JOIN teacher tc on tc.id = qa.appeal_correct_user_id and qa.correct_type = 2
LEFT JOIN con_user cu on cu.id = qa.appeal_correct_user_id and qa.correct_type = 4
WHERE 1=1 
#if(startTime)
and qa.create_at > :startTime
#end
#if(endTime)
and qa.create_at < :endTime
#end
#if(accountName)
and a.name like :accountName
#end 
#if(schoolName)
 and sl.name like :schoolName
#end
#if(correctName)
 and (tc.name like :correctName or cu.name like :correctName)
#end
#if(status)
 and qa.status =:status
#end
order by qa.create_at DESC
#end


##更新申述记录的状态
#macro($updateAppealStatus(id,status))
update question_appeal set status = :status where id = :id
#end
