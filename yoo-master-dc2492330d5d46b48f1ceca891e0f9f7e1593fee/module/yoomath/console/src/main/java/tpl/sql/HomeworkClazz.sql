#macro($zycGetClass(teacherId, name))
SELECT * FROM homework_class t WHERE t.name = :name AND t.teacher_id = :teacherId
#end

#macro($zycCountByCode(code))
SELECT count(id) FROM homework_class WHERE code = :code
#end

##查询当前老师有几个有效的班级
#macro($getClassNumByTeacher(teacherId))
SELECT count(id) FROM homework_class WHERE teacher_id = :teacherId and status = 0
#end

##查询班级
#macro($queryHkClazz(status,phaseCode,subjectCode,schoolName,accountName,teacherName,clazzName,code,schoolYear))
SELECT a.code,a.name classname,f.name schoolname,d.name accountName,b.school_id,c.user_channel_code,
		c.name teachername,a.student_num,a.status,e.name subjectname,a.id,a.school_year schoolYear
FROM homework_class a 
INNER JOIN teacher b ON a.teacher_id = b.id 
INNER JOIN USER c ON a.teacher_id = c.id
INNER JOIN account d ON c.account_id = d.id 
LEFT JOIN school f ON b.school_id = f.id
LEFT JOIN subject e ON b.subject_code = e.code
WHERE 1=1
	#if(status)
		AND a.status = :status
	#end
	#if(phaseCode)
		AND b.phase_code = :phaseCode
	#end
	#if(subjectCode)
		AND b.subject_code = :subjectCode
	#end
	#if(schoolName)
		AND f.name LIKE :schoolName
	#end
	#if(accountName)
		AND d.name LIKE :accountName
	#end
	#if(teacherName)
		AND b.name LIKE :teacherName
	#end
	#if(clazzName)
		AND a.name LIKE :clazzName
	#end
	#if(code)
		AND a.code LIKE :code
	#end
	#if(schoolYear)
		AND a.school_year = :schoolYear
	#end
	ORDER BY a.create_at DESC
#end

##查询某个班级的详情
#macro($getHkClazz(clazzId, clazzCode))
select a.id,a.code,a.name classname,b.name teachername,a.status,b.id teacherid,a.school_year schoolYear from homework_class a 
inner join teacher b on a.teacher_id = b.id
where 1=1
#if(clazzId)
 and a.id = :clazzId
#end
#if(clazzCode)
 and a.code = :clazzCode
#end
#end

## 查询
#macro($zycQuery(teacherId,status,size))
SELECT * FROM homework_class WHERE teacher_id = :teacherId
#if(status)
 AND status = :status
#end
ORDER BY id DESC
#if(size)
 limit :size
#end
#end

##分页获取homework_class
#macro($zycGetAllByPage())
SELECT * FROM homework_class WHERE status =0  AND  id < :next ORDER BY id DESC
#end

## 临时跑学生周报告要用，后续这个文件要删除
#macro($queryClassIdsByWeek())
SELECT id FROM homework_class WHERE id < :next AND status = 0
ORDER BY id DESC
#end