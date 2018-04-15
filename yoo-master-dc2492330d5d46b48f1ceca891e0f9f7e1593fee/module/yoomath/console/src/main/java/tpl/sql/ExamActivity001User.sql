## 期末活动后台中奖用户总数查询
#macro($csCountGiftUser(code))
SELECT count(*) FROM exam_activity_001_user WHERE activity_code =:code AND q_num != 0
#end

## 期末活动后台中中奖总Q点查询
#macro($csCountTotalQ(code))
SELECT sum(value0) FROM exam_activity_001_user WHERE activity_code =:code
#end

##活动用户查询
#macro($csQueryExamActivity01User(activityCode,accountName,realName,schoolName,phase,status))
select eau.id,eauq.create_at as lastTime, eauq.user_id as userId,t.phase_code, uc.name as channelName, sl.name as schoolName, a.name as accountName, u.name as realName,
eau.qq, eau.q_num as qNum, eau.view_q_num as viewQNum, SUM(eauq.value0 != 0 && eauq.viewed != 0) as win,eau.value0 as value,eau.received 
from exam_activity_001_user_q eauq
INNER JOIN exam_activity_001_user eau ON eau.user_id=eauq.user_id and  eau.activity_code=eauq.activity_code  
INNER JOIN user u ON u.id =eau.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT JOIN user_channel uc ON u.user_channel_code =uc.code
INNER JOIN student t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id
where eau.activity_code =:activityCode
#if(accountName)
and a.name like :accountName
#end 
#if(schoolName)
 and sl.name like :schoolName
#end
#if(realName)
 and u.name like:realName
#end
#if(status)
 and eau.received =:status
#end
#if(phase)
 and t.phase_code =:phase
#end
group by eauq.user_id 
order by eauq.create_at DESC
#end
