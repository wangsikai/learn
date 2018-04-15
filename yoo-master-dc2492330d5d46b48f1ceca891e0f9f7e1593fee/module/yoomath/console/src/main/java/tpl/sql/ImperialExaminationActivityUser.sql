##查询报名用户
#macro($queryActivityUser(activityCode,accountName,channelName,schoolName,grade,room,category))
select ieau.create_at,IFNULL(ieau.room,0) room,IFNULL(ieau.textbook_category_code,0) category,a.name as accountName,u.name as realName ,t.phase_code,uc.name as channelName,sl.name as schoolName,
ieau.name,ieau.mobile,ieau.grade,ieau.class_list from imperial_exam_activity_user ieau 
INNER JOIN user u ON u.id =ieau.user_id and u.status=0
INNER JOIN account a on  a.id=u.account_id
LEFT JOIN user_channel uc ON u.user_channel_code =uc.code INNER JOIN teacher t ON t.id =u.id 
LEFT JOIN school sl on t.school_id =sl.id 
where ieau.activity_code=:activityCode
#if(accountName)
and a.name like :accountName
#end
#if(channelName)
and uc.name like :channelName
#end
#if(schoolName)
 and sl.name like:schoolName
#end
#if(grade)
 and ieau.grade =:grade
#end
#if(room)
 and ieau.room =:room
#end
#if(category)
 and ieau.textbook_category_code =:category
#end
order by ieau.create_at desc
#end

