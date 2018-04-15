## 分页查询学生数据
#macro($csQuery(accountName,channelName,schoolName,userName,phaseCode,memberType,memberType,classId))
SELECT t.* FROM student t
INNER JOIN user u ON t.id = u.id
INNER JOIN account a ON a.id = u.account_id
LEFT JOIN school s ON t.school_id = s.id
LEFT JOIN user_channel uc ON u.user_channel_code = uc.code
LEFT JOIN user_member um ON um.user_id = u.id
#if(classId)
INNER JOIN homework_student_class hsc ON hsc.student_id = u.id
#end
WHERE u.status = 0
#if(accountName)
AND a.name LIKE :accountName
#end
#if(channelName)
AND uc.name LIKE :channelName
#end
#if(schoolName)
AND s.name LIKE :schoolName
#end
#if(userName)
AND u.name LIKE :userName
#end
#if(phaseCode)
AND t.phase_code = :phaseCode
#end
#if(memberType)
  #if(memberType==0)
  AND um.id IS NULL
  #else
  AND um.member_type = :memberType AND um.end_at >= :nowDate
  #end
#end
#if(classId)
AND hsc.class_id = :classId AND hsc.status = 0
#end
ORDER BY t.id DESC
#end