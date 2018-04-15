## 查询学校数据
#macro($csQuerySchool(code,channelName,schoolName,schoolType))
SELECT t.* FROM school t
INNER JOIN channel_school c ON t.id = c.school_id
INNER JOIN user_channel u ON u.code = c.channel_code
WHERE t.status = 0 AND t.type > 1
#if(code)	
AND c.channel_code = :code
#end
#if(schoolName)
AND t.name LIKE :schoolName
#end
#if(schoolType)
AND t.type = :schoolType
#end
#if(channelName)
AND u.name LIKE :channelName
#end
#end

## 查询学校列表
#macro($csQuerySchoolList(code,type,schoolName))
select * from school where 1=1
	#if(code)
		and district_code like :code
	#end
	#if(type)
		and type =:type
	#end
	#if(schoolName)
		and name like:schoolName
	#end
#end

## 判断当前渠道是否已经存在
#macro($csChannelCount(code))
	select count(code) from user_channel where code =:code
#end

## 根据教师id查找教师所在学校数据
#macro($csFindByTeacher(teacherId))
SELECT t.* FROM school t INNER JOIN teacher o ON o.school_id = t.id
WHERE o.id = :teacherId
#end

## 根据多个教师id列表查找所有学校数据
#macro($csFindByTeachers(teacherIds))
SELECT t.id AS school_id, o.id AS teacher_id, t.name AS school_name, t.code AS school_code
FROM school t INNER JOIN teacher o ON o.school_id = t.id
WHERE o.id IN :teacherIds
#end

## 查询学校各用户数量
#macro($csCountUserNum(schoolIds,userType))
SELECT s.id AS school_id, count(t.id) AS user_num
FROM school s
#if(userType==1)
INNER JOIN teacher t ON t.school_id = s.id
#end
#if(userType==2)
INNER JOIN student t ON t.school_id = s.id
#end
INNER JOIN user u ON u.id = t.id
  WHERE s.id IN :schoolIds AND u.status = 0
GROUP BY s.id
#end

## 查询学校里教师会员数量
#macro($csCountTeacherMemberNum(nowDate,schoolIds))
SELECT s.id AS school_id, count(m.id) AS member_num
FROM school s
INNER JOIN teacher t ON s.id = t.school_id
INNER JOIN user u ON t.id = u.id
INNER JOIN account a ON a.id = u.account_id
INNER JOIN user_member m ON m.user_id = t.id
WHERE u.status = 0 AND a.status = 0 AND m.end_at >= :nowDate AND s.id IN :schoolIds
AND m.member_type > 0
GROUP BY s.id
#end

## 查询学校里学生会员数量
#macro($csCountStudentMemberNum(nowDate,schoolIds))
SELECT s.id AS school_id, count(m.id) AS member_num
FROM school s
INNER JOIN student t ON s.id = t.school_id
INNER JOIN user u ON u.id = t.id
INNER JOIN account a ON a.id = u.account_id
INNER JOIN user_member m ON m.user_id = t.id
WHERE u.status = 0 AND a.status = 0 AND m.end_at >= :nowDate AND s.id IN :schoolIds
AND m.member_type > 0
GROUP BY s.id
#end