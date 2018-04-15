## 查找班级数据
#macro($csFindClass(code,channelName,className,schoolName,schoolId))
SELECT t.* FROM homework_class t
INNER JOIN user u ON u.id = t.teacher_id
INNER JOIN teacher e ON e.id = u.id
INNER JOIN school s ON s.id = e.school_id
INNER JOIN user_channel uc ON uc.code = u.user_channel_code
WHERE
t.status < 2 AND u.user_channel_code > 10000
#if(code)
AND uc.code = :code
#end
#if(channelName)
AND u.name LIKE :channelName
#end
#if(className)
AND t.name LIKE :className
#end
#if(schoolName)
AND s.name LIKE :schoolName
#end
#if(schoolId)
AND s.id = :schoolId
#end
#end


## 查找班级数据
#macro($csQueryClassInfo(classId))
	SELECT a.name class_name,b.name teacher_name FROM homework_class a 
	INNER JOIN teacher b ON a.teacher_id = b.id
	WHERE a.id = :classId
#end

## vip个数
#macro($csCountVip(classId,nowDate))
	 SELECT COUNT(*) FROM user_member a 
 	INNER JOIN homework_student_class b ON a.user_id = b.student_id 
 	AND b.class_id = :classId and b.status = 0
 	AND DATE_FORMAT(a.end_at,'%Y-%m-%d 23:59:59') >= :nowDate
#end


## 学生个数
#macro($csCountStu(classId))
	 SELECT COUNT(*) FROM homework_student_class a
	 inner join user b on a.student_id = b.id
 	 AND a.class_id = :classId and b.status = 0 and a.status = 0
#end

## 学生个数查询（去除账号被禁用统计homework_clazz中的class_num不准确）
#macro($csCountClassStus(classIds))
SELECT count(u.id) AS stu_num, h.class_id AS id
FROM user u INNER JOIN student s ON s.id = u.id
INNER JOIN account a ON a.id = u.account_id
INNER JOIN homework_student_class h ON h.student_id = u.id
WHERE u.status = 0 AND a.status = 0 AND h.class_id IN :classIds
GROUP BY h.class_id
#end


## 查询
#macro($csFindByTeacherIds(teacherIds))
SELECT * FROM homework_class WHERE teacher_id in (:teacherIds) AND status = 0
#end