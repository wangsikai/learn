## 分页查询教师数据
#macro($csQuery(accountName,channelName,schoolName,userName,phaseCode,schoolId,memberType,nowDate))
SELECT t.* FROM teacher t
INNER JOIN user u ON t.id = u.id
INNER JOIN account a ON a.id = u.account_id
#if(schoolId)
INNER JOIN school s ON t.school_id = s.id
#else
LEFT JOIN school s ON t.school_id = s.id
#end
LEFT JOIN user_channel uc ON u.user_channel_code = uc.code
LEFT JOIN user_member um ON um.user_id = u.id
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
#if(schoolId)
AND t.school_id = :schoolId
AND u.user_channel_code > 10000
#end
#if(memberType)
  #if(memberType==0)
  AND um.id IS NULL
  #else
  AND um.member_type = :memberType AND um.end_at >= :nowDate
  #end
#end
ORDER BY t.id DESC
#end