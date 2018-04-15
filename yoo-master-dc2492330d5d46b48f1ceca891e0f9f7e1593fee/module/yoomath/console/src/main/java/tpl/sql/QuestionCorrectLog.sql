##查询题目批改的记录
#macro($zycFindQuestionCorrectLog(accountName,homeworkId,sequence,newCorrect))
SELECT qcl.create_at as createAt,qcl.result,qcl.correct_type as correctType,IFNULL(tc.name,cu.name) as correctName,shq.new_correct as newCorrect
from question_correct_log qcl
INNER JOIN student_homework_question shq on shq.id = qcl.student_homework_question_id 
INNER JOIN student_homework sh on sh.id = shq.student_homework_id 
INNER JOIN homework hk on hk.id = sh.homework_id
INNER JOIN homework_question hkq on hkq.homework_id = hk.id and hkq.question_id = shq.question_id
INNER JOIN user u ON u.id =sh.student_id and u.status=0
INNER JOIN account a on a.id=u.account_id
LEFT JOIN teacher tc on tc.id = qcl.user_id and qcl.correct_type in (2,3)
LEFT JOIN con_user cu on cu.id = qcl.user_id and qcl.correct_type = 4
WHERE 1=1 
#if(accountName)
and a.name = :accountName
#end 
#if(homeworkId)
 and hk.id = :homeworkId
#end
#if(sequence)
 and hkq.sequence = :sequence
#end
#if(newCorrect)
 and shq.new_correct =:newCorrect
#end
order by qcl.create_at DESC
#end
