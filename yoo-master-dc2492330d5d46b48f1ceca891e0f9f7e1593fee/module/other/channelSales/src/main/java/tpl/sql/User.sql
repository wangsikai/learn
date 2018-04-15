##分页查询
#macro($csQuery())
SELECT * FROM user_channel ORDER BY code DESC
#end


##渠道用户数据
#macro($csGetUsers(code,phaseCode,userType,memberType,sex,schoolId,accountName,userName,mobile,nowDate))
select CONCAT(u.id,'') as id,IFNULL(t.phase_code,s.phase_code) as phase_code,
u.user_type,IFNULL(m.member_type,0) as member_type,m.start_at as memberStart,end_at as memberEnd,
IFNULL(t.school_id,s.school_id) as schoolId,u.name,a.mobile,a.email,a.name as accountName,
IFNULL(t.sex,s.sex) as sex,u.status,s.birthday,sb.name as subjectName,d.name as dutyName,
t2.name as titleName,t.work_at,IFNULL(t.create_at,s.create_at) as createat,us.homework_sms from user u 
LEFT  JOIN   teacher t on u.id=t.id 
LEFT  JOIN  user_settings us on t.id=us.id  
LEFT  JOIN  student s on u.id=s.id 
LEFT  JOIN subject sb ON t.subject_code = sb.code 
LEFT  JOIN duty d ON t.duty_code = d.code 
LEFT  JOIN title t2 ON t.title_code = t2.code 
LEFT  JOIN user_member m on m.user_id=u.id 
INNER JOIN account a on u.account_id=a.id where u.user_channel_code =:code and u.status=0
#if(phaseCode)
  and (t.phase_code =:phaseCode or s.phase_code=:phaseCode) 
#end
#if(userType)
  and u.user_type=:userType
#end
#if(memberType==0)
  and  (m.end_at < :nowDate or m.end_at IS NULL)
 #else(memberType==1)
  and m.member_type=:memberType and  ((m.start_at <= :nowDate AND m.start_at IS NOT NULL) AND (m.end_at >= :nowDate AND m.end_at IS NOT NULL))
 #else(memberType==2)
  and m.member_type=:memberType and  ((m.start_at <= :nowDate AND m.start_at IS NOT NULL) AND (m.end_at >= :nowDate AND m.end_at IS NOT NULL))
#end
#if(sex)
  and  (t.sex =:sex or s.sex=:sex) 
#end
#if(schoolId)
  and  (t.school_id=:schoolId or s.school_id=:schoolId)
#end
#if(accountName)
  and a.name=:accountName
#end
#if(mobile)
  and a.mobile=:mobile
#end
#if(userName)
  and u.name =:userName
#end
#end

## 查找渠道教师数据
#macro($csFindTeacherUserBySchool(schoolId))
SELECT u.* FROM user u INNER JOIN teacher t ON t.id = u.id
WHERE t.school_id = :schoolId AND u.user_channel_code > 10000 AND u.status = 0
#end

## 查找渠道学生数据
#macro($csFindStudentUserBySchool(schoolId))
SELECT u.* FROM user u INNER JOIN student t ON t.id = u.id
WHERE t.school_id = :schoolId AND u.user_channel_code > 10000 AND u.status = 0
#end

## 根据登录名列表查找用户列表
#macro($csFindByAccountNames(names))
SELECT u.*, t.name FROM user u INNER JOIN account t ON t.id = u.account_id
WHERE t.name IN :names AND u.status = 0
#end

## 根据条件查询用户
#macro($csQueryUserList(channelCode,classId,accountName,memberType,name,schoolName,userType,phaseCode,activationStatus,nowDate,clazzName))
SELECT t.*,d.name accountName,e.name schoolname,d.activation_status
			#if(memberType == 0)
				,0 as member_type
			#elseif
				,z.member_type,z.start_at,z.end_at
			#end
	FROM(
	#if(userType != 2)
			SELECT a.id,a.user_type,c1.sex,c1.create_at,a.status,a.name,a.account_id,a.user_channel_code,
			 	c1.phase_code,c1.school_id
			FROM USER a
			INNER JOIN teacher c1 ON a.id = c1.id 
			#if(classId)
				INNER JOIN homework_class dd ON dd.teacher_id = c1.id AND dd.id = :classId
			#end
			#if(phaseCode)
				AND c1.phase_code = :phaseCode
			#end
			where a.status = 0
				#if(status)
					and a.status = :status
				#end
				#if(name)
					and a.name like :name
				#end
				#if(channelCode)
					and a.user_channel_code = :channelCode
				#end
				#if(clazzName)
					AND EXISTS (
						SELECT 1 FROM homework_class dd where dd.teacher_id = c1.id and dd.status=0 and dd.name like :clazzName
					)
				#end
	#end
	#if(userType == 99)
		union
	#end
	#if(userType != 1)
		SELECT a.id,a.user_type,c2.sex,c2.create_at,a.status,a.name,a.account_id,a.user_channel_code,
			c2.phase_code,c2.school_id
		FROM USER a
		INNER JOIN student c2 ON a.id = c2.id 
		#if(classId)
			INNER JOIN homework_student_class ff ON ff.student_id = c2.id AND ff.class_id =:classId and ff.status = 0
			inner join homework_class dd on ff.class_id = dd.id
		#end
		#if(phaseCode)
			AND c2.phase_code = :phaseCode
		#end
		where a.status = 0
		#if(status)
			and a.status = :status
		#end
		#if(name)
			and a.name like :name
		#end
		#if(channelCode)
			and a.user_channel_code = :channelCode 
		#end
		#if(clazzName)
			AND EXISTS (
				SELECT 1 FROM homework_student_class ff
				INNER JOIN homework_class dd ON ff.class_id = dd.id and dd.status = 0 and dd.name like :clazzName
				WHERE ff.student_id = c2.id AND ff.status = 0
			)
		#end
		#end
) t 
	INNER JOIN account d ON t.account_id = d.id 
		#if(accountName)
			AND d.name like :accountName
		#end
		#if(activationStatus)
			AND d.activation_status = :activationStatus
		#end
	#if(schoolName)
	INNER JOIN school e ON t.school_id = e.id  AND e.name like :schoolName
	#elseif
	LEFT JOIN school e ON t.school_id = e.id
	#end
	#if(memberType)
		#if(memberType == 0)
			WHERE t.id NOT IN (SELECT user_id FROM user_member z WHERE z.member_type != 0 and DATE_FORMAT(z.end_at,'%Y-%m-%d 23:59:59') >= :nowDate)
		#elseif(memberType != 0)
			INNER JOIN user_member z ON t.id = z.user_id AND member_type = :memberType and DATE_FORMAT(z.end_at,'%Y-%m-%d 23:59:59') >= :nowDate
		#end
	#elseif
		LEFT JOIN user_member z ON t.id = z.user_id
	#end
	ORDER BY t.id DESC
#end